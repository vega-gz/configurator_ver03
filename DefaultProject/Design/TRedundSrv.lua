local Delay_cycle			= 6 			-- задержка (сек) между циклами основного приложения
local Cfg_cycle				= 3				-- количество попыток опроса узла для проверки наличия связи с ним
local Cfg_delay				= 0.5			-- задержка на повторную проверку связи с узлом
local Name_MAIN_Server		= 'V_SRV1.Loader'-- имя узла основного сервера
local Name_SECOND_Server	= 'V_SRV2.Loader'-- имя узла резервнго сервера
local App_PLCs =							-- Название приложений мнемосхем на PLCах
					{'GPA4.Loader','GPA5.Loader','GPA6.Loader','KC1_Main.Loader','NMS1_Main.Loader','UPA1_Main.Loader'}
local App_ARMs =							-- Название приложений мнемосхем на АРМах
					{'ARM1.Loader','ARM2.Loader','ARM3.Loader','ARM4.Loader'}
local Event_ID				= 10100			-- ID сообщения для события на мнемосхеме
local Log_ON				= true			-- Писать ли логи?
----inv
--local NameApp = {'Рапорт','Рапорт_Lua','Tunefile','Архив','Фиксация_событий','Мнемосхема_Сервер'}

-----------------------------------------------------------------------------------------------
-------------------------------КОНЕЦ КОНФИГУРАЦИОННЫХ ПАРАМЕТРОВ-------------------------------
-----------------------------------------------------------------------------------------------
local Name_CUR_Server	= Core.getName()    --Core['NameCurServer'] -- название текущего узла
Core['Toladka_curS'] = Name_CUR_Server
local Name_PARTNER_Server	= '' 			-- название приложения-партнера по резерву 
local Numb_CUR_Server		= 1				-- номер текущего сервера
 Link_PLCs_ok_prev		= false 		-- есть связь хотя бы с 1 PLC на предыдущем цикле
 Link_partner_ok_prev 	= false			-- есть связь с узлом-партнёром по резерву на предыдущем цикле
local Cur_server_work_prev

--2019.07.05 
-- поехали, тут будут переменные для подсчета и инициализации рабочих приложений на узле
--вводим переменные 
local S1 = 'V_SRV1'	-- первый сервер \\длинное название надо урезать
local S2 = 'V_SRV2'	-- второй сервер

--Создаем таблицу с данными по узлу
local TipS1			=	{"172.16.1.101"} 	-- перечисляем IP адреса узла
local TAppPortS1 	= 	{"10001","10002","10003","10004","10005","10006","10007","10008","10009","10010","10011","10012","10013","10014","10015","10016"}
												
local TipS2			=	{"172.16.1.104"} 	-- перечисляем IP адреса узла
local TAppPortS2 	= 	{"10001","10002","10003","10004","10005","10006","10007","10008","10009","10010","10011","10012","10013","10014","10015","10016"}

-- Таблица приложений связи
--local NodeTable	=	   {{NodeName = 'Server1',	NodeIP = {'192.168.61.140'},	NodePort ='10105'},
--						{NodeName = 'Server2',	NodeIP = {'192.168.61.133'},	NodePort ='10105'}
--						}
if 	string.find(Name_CUR_Server, "_2")~=nil then   Core.directSet(TipS2[1]..':10000', 1, '@RESERVE', 0, true, "BOOL")  os.sleep(60)  end
-----------------------------------------------------------------------------------------------
------------------------------------НАЧАЛО ОПИСАНИЯ ФУНКЦИЙ------------------------------------
-----------------------------------------------------------------------------------------------
--начало тестовых функций

-- Функция подсчета количесва работающих/резервных приложений
 function getStatServ1()					-- считаем кол-во приложений в сервере
	local numAppS1	= 0							-- обнуляем перед запуском кол-во приложений
		for i=1, #TipS1 do
			local _,loaderS1,_,_,_ = Core.directGet(TipS1[i]..':10000', 1, '@STATE', 0)
		
		  if loaderS1 ~= nil then 
			for j=1, #TAppPortS1 do				-- прогоняем все порты приложений узла
				local _,valS1,_,_, _ = Core.directGet(TipS1[i]..':'..TAppPortS1[j], 0.1, '@STATE', 0)
				if valS1 ~= nil and (string.find(valS1,'RUN')~= nil or string.find(valS1,'RES')~= nil)
					then numAppS1 = numAppS1 + 1 
				end
					if StApp ~= nil and not ( string.find(StApp,'RUN')~= nil or string.find(StApp,'RES')~= nil ) -- добавляем сообщение о неисправности сообщения
						then  Core.addLogMsg(tostring(S1)..Numb_CUR_Server..tostring(TipS1[i]..':'..TAppPortS1[j]))
								Core.addEvent('Приложение(я) не в состоянии "Готов". Сервера: '..tostring(S1), -- сообщение
											Event_ID,																		-- категоря сообщения
											1, 																				-- состояние события (0 - исчезло, 1 - возникло)
											(Numb_CUR_Server == 1) and 'Сервер 1' or 'Сервер 2',							-- источник события
											'Система',																		-- имя вызвавшего событие
											Numb_CUR_Server..tostring(TipS1[i]..':'..TAppPortS1[j])		-- Идентификатор
											) 
								 end
			end
            break 
          end
		end
		return numAppS1
end	


 function getStatServ2()					-- считаем кол-во приложений в сервере
	local numAppS2	= 0							-- обнуляем перед запуском кол-во приложений
		for i=1, #TipS2 do 						-- прогоняем все IP что есть
				local _,loaderS2,_,_,_ = Core.directGet(TipS2[i]..':10000', 1, '@STATE', 0)

			if  loaderS2 ~= nil then 	-- пингуем (если хотя бы один пингуется, проверяем его)
			for j=1, #TAppPortS2 do				-- прогоняем все порты приложений узла
				local _,valS2,_,_, _ = Core.directGet(TipS2[i]..':'..TAppPortS2[j], 0.1, '@STATE', 0)
				if valS2 ~= nil and (string.find(valS2,'RUN')~= nil or string.find(valS2,'RES')~= nil)
					then numAppS2 = numAppS2 + 1 
				end
					if StApp ~= nil and not ( string.find(StApp,'RUN')~= nil or string.find(StApp,'RES')~= nil ) -- добавляем сообщение о неисправности сообщения
						then  	Core.addLogMsg(tostring(S2)..Numb_CUR_Server..tostring(TipS2[i]..':'..TAppPortS2[j]))
								Core.addEvent('Приложение(я) не в состоянии "Готов". Сервера: '..tostring(S2), -- сообщение
											Event_ID,																		-- категоря сообщения
											1, 																				-- состояние события (0 - исчезло, 1 - возникло)
											(Numb_CUR_Server == 1) and 'Сервер 1' or 'Сервер 2',							-- источник события
											'Система',																		-- имя вызвавшего событие
											Numb_CUR_Server..tostring(TipS2[i]..':'..TAppPortS2[j])		-- Идентификатор
											) end
			end
         break 
         end
		end
		return numAppS2
end	

-- проверяем кол-во рабочих IP на Узлах
--[[function IPnumS1()	-- пингуем Первый сервер
	local pingS1 = 0
 	for i=1, #TipS1 do
 		if os.ping(TipS1[i]) == true then 
 			pingS1 = pingS1 + 1
 		end
 	end
 	return pingS1
 end 

function IPnumS2()	-- пингуем Первый сервер
	local pingS2 = 0
 	for i=1, #TipS2 do
 		if os.ping(TipS2[i]) == true then 
 			pingS2 = pingS2 + 1
 		end
 	end
 	return pingS2
 end 
]]


--[[local function get_link_Node()
	Core['numCon']	= 0
		for i=1, #NodeTable do
			for j=1, #NodeTable[i].NodeIP do
				local _,StCon,_,_,_ = Core.directGet(NodeTable[i].NodeIP[j]..':'..NodeTable[i].NodePort, 0.1, '@STATE', 0)
				if StCon ~= nil and string.find(StCon, 'READY') then Core['numCon'] = Core['numCon'] + 1 end
			end
		end
	return Core['numCon']
end]]

--конец тестовых функций
---
--[[local function get_diag_App(S,App_at_S,numApp ) --подсчет количества приложений
 Core['numApp'] =0
 for i=1,#App_at_S do 
  local fullNameApp = S..'.'..App_at_S[i]
  local _,StApp,_,_,_ = Core.directGet(fullNameApp, 0.1, '@STATE', 0)
  if StApp ~= nil and ( string.find(StApp,'RUN')~= nil or string.find(StApp,'RES')~= nil ) then Core['numApp'] = Core['numApp'] +1  end
 end
end]]

--[[local function get_diag_con(Con_at_S,numCon ) --подсчет количества приложений
 Core['numCon'] =0
 for i=1,#Con_at_s do 
   local NameCon = Con_at_S[i]
   local _,StCon,_,_,_ = Core.directGet(NameCon, 0.1, '@STATE', 0)
  if StCon ~= nil and ( string.find(StCon,'RUN')~= nil or string.find(StCon,'RES')~= nil ) then Core['numCon'] = Core['numCon'] +1  end
 end
end]]

-- функция ввода/вывода сервера из резерва
local function control_reserve()
	local Command = Core['Cur_server_work'] ~= Numb_CUR_Server
	if Core['@COMMAND']~="OK" then
		Core.setReserve(Name_CUR_Server, Command)
	end
	local text1 = (Numb_CUR_Server == 1) and 'Сервер 1' or 'Сервер 2'
	local text2 = Command and ' выведен в резерв' or ' выведен в работу'
	local text = text1..text2
	Core.addEvent(text,
				Event_ID,
				1,
				(Numb_CUR_Server == 1) and 'Сервер 1' or 'Сервер 2',
				'Система',
				Numb_CUR_Server..tostring(Command))
	if Log_ON then
		Core.addLogMsg(text)
	end
end

-- функция инициализацции
local function Init()
	-- проверяем первый ли запуск и инициализируем Cur_server_work, если необходимо
	local numb_main_server = Core['Cur_server_work']
	if not numb_main_server or numb_main_server < 1 or numb_main_server > 2 then
		numb_main_server = 1 -- им становится первый
		Core['Cur_server_work'] = numb_main_server
		Cur_server_work_prev = 1
		Core.directSet(TipS2[1]..':10000', 1, '@RESERVE', 0, true, "BOOL")
--		Core.directSet(S2..'.Loader', 1, '@RESERVE', 0, true, "BOOL")
		
	end
    os.sleep(15)
	control_reserve()
end

-- функция контроля связи с остальными узлами проекта
-- функция контроля связи с остальными узлами проекта
local function check_link()
	local Link_ARMs = false
	local Link_PLCs = false
	local Link_partner = false
	local numb_cycle = 0


	while (numb_cycle < Cfg_cycle) and not Link_ARMs do
		for _,y in ipairs(App_ARMs) do
			Core.addLogMsg(y)
			if Core.getLatency(y) ~= -1 then
				Link_ARMs = true
				break
			end
		end
		numb_cycle = numb_cycle + 1
		if not Link_ARMs and (numb_cycle < Cfg_cycle) then
			os.sleep(Cfg_delay)
		end
	end
	
	numb_cycle = 0
	while (numb_cycle < Cfg_cycle) and not Link_PLCs do
		for _,y in ipairs(App_PLCs) do
			if Core.getLatency(y) ~= -1 then
				Link_PLCs = true
				break
			end
		end
		numb_cycle = numb_cycle + 1
		if not Link_PLCs and (numb_cycle < Cfg_cycle) then
			os.sleep(Cfg_delay)
		end
	end

	numb_cycle = 0
	while (numb_cycle < Cfg_cycle) and not Link_partner do
		if Core.getLatency(Name_PARTNER_Server) ~= -1 then Link_partner = true end
		numb_cycle = numb_cycle + 1
		if not Link_partner and (numb_cycle < Cfg_cycle) then
			os.sleep(Cfg_delay)
		end
	end

	return Link_ARMs, Link_PLCs, Link_partner
end
-----------------------------------------------------------------------------------------------
------------------------------------КОНЕЦ ОПИСАНИЯ ФУНКЦИЙ-------------------------------------
-----------------------------------------------------------------------------------------------
if Delay_cycle < 0 then Delay_cycle = 4 end
if Cfg_cycle <= 0 then Cfg_cycle = 1 end
if Cfg_delay < 0 then Cfg_delay = 0.1 end

-- Определяем тип текущего узла (основной/резервный)
if Name_CUR_Server == Name_MAIN_Server then
	Name_PARTNER_Server = Name_SECOND_Server
	Numb_CUR_Server = 1
elseif Name_CUR_Server == Name_SECOND_Server then
		Name_PARTNER_Server = Name_MAIN_Server
		Numb_CUR_Server = 2
	else
		if Log_ON then
			Core.addLogMsg('Ошибка конфигурации. Название узла ('..Name_CUR_Server..') не совпадает ни с ('..Name_MAIN_Server..'), ни с ('..Name_SECOND_Server..')')
		end
		return
end

Init()


while true do
	local Link_ARMs_ok 		-- есть связь хотя бы с 1 АРМом
	local Link_PLCs_ok 		-- есть связь хотя бы с 1 PLC
	local Link_partner_ok 	-- есть связь с узлом-партнёром по резерву
    last_App_S1 = Core['S1_Work_App']
    last_App_S2 = Core['S2_Work_App']
--    last_Con_S1 = Core['S1_Work_Con']
--    last_Con_S2 = Core['S2_Work_Con']

Link_ARMs_ok, Link_PLCs_ok, Link_partner_ok = check_link()
	Link_ARMs_ok = true --NIO
-- считаем кол-во приложений
	--local Serv1app, Serv1ping = getStatServ1()
	--local Serv2app, Serv2ping = getStatServ2()
	Core['S1_Work_App'] = getStatServ1()
--	Core['S1_Work_Con'] = IPnumS1()
	Core['S2_Work_App'] = getStatServ2()
--	Core['S2_Work_Con'] = IPnumS2()
    Core['S1PingString']=''..Numb_CUR_Server
    Core['S2PingString']=''..Numb_CUR_Server

--


-- считаем кол-во связей
	
	--Link_ARMs_ok	= Core.Local_Link_ARM
	--Link_PLCs_ok	= Core.Local_Link_PLC
	--Link_partner_ok	= Core.Local_Link_Partner

-------------------	if not Link_partner_ok then
		--if Link_ARMs_ok then
-------------------			Core['Cur_server_work'] = Numb_CUR_Server
		--else
		--	Core['Cur_server_work'] = (Numb_CUR_Server == 1) and 2 or 1
		--end
-------------------	else
-------------------		Core['Cur_server_work'] = 1
-------------------	end

--------inv, закоментил //smr 2019.07.07 13:45
--[[   if Core['NumServ'] == 1 then   
--    get_diag_App_test(S1, NameApp, S1_Work_App)
--    get_diag_con(Con_at_S,S1_Work_Con )
   end   

   if Core['NumServ'] == 2 then       
--    get_diag_App(S2,NameApp,S2_Work_App )
--    get_diag_con(Con_at_S,S2_Work_Con )
   end]]

-------------------   if  (Core['S1_Work_App'] <= last_App_S1  or Core['S1_Work_Con'] <= last_Con_S1) and Core['Cur_server_work']==1 and (Core['S1_Work_App'] + Core['S1_Work_Con'] < Core['S2_Work_App'] +Core['S2_Work_Con'] )  then Core['Cur_server_work']=2 end
-------------------   if  (Core['S2_Work_App'] <= last_App_S2  or Core['S2_Work_Con'] <= last_Con_S2) and Core['Cur_server_work']==2 and (Core['S1_Work_App'] + Core['S1_Work_Con'] > Core['S2_Work_App'] +Core['S2_Work_Con'] )  then Core['Cur_server_work']=1 end

--взводим флаги "работа" на серверах
if Name_CUR_Server == Name_MAIN_Server then Core['Work_S1'] = true 
						   				else Core['Work_S2'] = true
end
--проверяем работу верхнего и нижнего уровня сетей на серверах
if (not Link_ARMs_ok or not Link_PLCs_ok) then 
	if Name_CUR_Server == Name_MAIN_Server then Core['Work_S1'] = false 
											else Core['Work_S2'] = false 
	end
		
end

--выбираем основной сервер
	if not Link_partner_ok then
		--if Link_ARMs_ok then
			Core['Cur_server_work'] = Numb_CUR_Server
		else
		--	Core['Cur_server_work'] = (Numb_CUR_Server == 1) and 2 or 1
		--end
					
		if (not Link_ARMs_ok or not Link_PLCs_ok) and (Core['Work_S1'] == true or Core['Work_S2'] == true) then 
			if Name_CUR_Server == Name_MAIN_Server then Core['Cur_server_work'] = 2 
													else Core['Cur_server_work'] = 1 
			end 
												 
		end
				if 	(Core['Work_S1'] == true and Core['Work_S2'] == true) then --Core['Cur_server_work'] = 2 
																			   if  (Core['S1_Work_App'] <= last_App_S1) and Core['Cur_server_work']==1 and (Core['S1_Work_App'] < Core['S2_Work_App'])  then Core['Cur_server_work']=2 end
																			   if  (Core['S2_Work_App'] <= last_App_S2) and Core['Cur_server_work']==2 and (Core['S1_Work_App'] > Core['S2_Work_App'])  then Core['Cur_server_work']=1 end
				end
				if 	(Core['Work_S1'] == false and Core['Work_S2'] == false) then Core['Cur_server_work'] = 2 end


	end
--Взводим флаги "работа нижней сети" на серверах
if Name_CUR_Server == Name_MAIN_Server then Core['NoLink.Server1_N'] = false 
						   				else Core['NoLink.Server2_N'] = false
end
--проверяем работу нижнего уровня сетей на серверах
if  not Link_PLCs_ok then 
	if Name_CUR_Server == Name_MAIN_Server then Core['NoLink.Server1_N'] = true 
											else Core['NoLink.Server2_N'] = true 
	end
end

--Проверяем, все ли приложения на главном сервере работают

if Core['Cur_server_work'] == 1 and Core['S1_Work_App'] < #TAppPortS1 then Core['Server_rab'] = false end

if Core['Cur_server_work'] == 2 and Core['S2_Work_App'] < #TAppPortS1 then Core['Server_rab'] = false end

if Core['Cur_server_work'] == 1 and Core['S1_Work_App'] == #TAppPortS1 then Core['Server_rab'] = true end

if Core['Cur_server_work'] == 2 and Core['S2_Work_App'] == #TAppPortS1 then Core['Server_rab'] = true end


	--Core.onExtChange({'Cur_server_work'}, control_reserve, {})
	if Cur_server_work_prev ~= Core['Cur_server_work'] then control_reserve() end




	Cur_server_work_prev = Core['Cur_server_work']
    
	Link_ARMs_ok_prev 	 = Link_ARMs_ok
	Link_PLCs_ok_prev 	 = Link_PLCs_ok
	Link_partner_ok_prev = Link_partner_ok
	Core.Local_Link_ARM		= Link_ARMs_ok
	Core.Local_Link_PLC 	= Link_PLCs_ok
	Core.Local_Link_Partner = Link_partner_ok
	os.sleep(Delay_cycle)
end