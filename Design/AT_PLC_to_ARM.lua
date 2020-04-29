-------------------------------------------------------------------------------------------
-- Наименование режима
function GetMode()

	local TagName_m = {
					-- Переменная режима
					'Mode.Test',
   				}
	local TagComment_m = {}

	for i=1, #TagName_m do
		table.insert(TagComment_m, Core.getSignalComment(TagName_m[i]));
	end

	for j = 1 , #TagName_m do 
	 	if Core[""..TagName_m[j]..""] == true then Core["MnemoSystSign.Mode_String"] = TagComment_m[j] end
	end


	if Core["Mode.Test"] then Core["MnemoSystSign.ModeColor"] = 1 end

	--[[	кооментим пока нету  барабанов 
	 if Core["UnitState"] > 100 and Core["UnitState"] < 200 then Core["Mode_Color"] = 1 end
	 if Core["UnitState"] > 200 and Core["UnitState"] < 300 then Core["Mode_Color"] = 3 end
	 if Core["UnitState"] > 300 and Core["UnitState"] < 400 then Core["Mode_Color"] = 2 end
	 if Core['TS.OZ_MG']  or Core['TS.OZ_HH'] or Core['TS.RingOZ']   then Core["Mode_Color"] = 4 end
	 ]]
end

-------------------------------------------------------------------------------------------
-- Наименование Техгологического сообщения
function GetStatus()
	local ConString
	local TagName_s = 	{
	'TS.testP',
	}

	local TagComment_s = {}
	local ConString ={}

	for i=1, #TagName_s do
		table.insert (TagComment_s, Core.getSignalComment(TagName_s[i]))
	end


	if not Core['MnemoSystSign.no_link_CPU'] then
		for j = 1 , #TagName_s  do
		  if Core[""..TagName_s[j]..""] == true then  table.insert( ConString, TagComment_s[j]) end;
		end;
	Core["MnemoSystSign.Status_String"] = table.concat(ConString, "\n") 
		else
	Core["MnemoSystSign.Status_String"] = ''
	end
end

-------------------------------------------------------------------------------------------
-- Вывод таймеров
function GetStatusTMR(DBname_t,DBname_t1, firstID_t,appName_t)

	local TagName_t = {
	'TMR.prodN',
	}

	local TagComment_t = {}
	local ConString_t ={}

	local TagName_chang_t ={
	'ustT.prodN',
	}

	local Status_t

	for i=1, #TagName_t do
		table.insert(TagComment_t, Core.getSignalComment(TagName_t[i]))
	end


	for j = 1 , #TagName_t  do
	  if Core[""..TagName_t[j]..""] ~= Core[""..TagName_chang_t[j]..""]  and  Core[""..TagName_t[j]..""] ~= 0
	     then  Status_t = string.gsub(tostring(Core[""..TagName_chang_t[j]..""]  - Core[""..TagName_t[j]..""]),"(%p)(.+)","" )                                  
	           table.insert( ConString_t, TagComment_t[j].." - "..Status_t) end;
	end;
	Core["Status_Name_TMR"] = table.concat(ConString_t, "\n") 
end

-------------------------------------------------------------------------------------------
local function get_ust(par)
	UnitState = GetMode()		-- Режим
	TS   	   = GetStatus()	-- Технологические сообщения
	--Drum      = drum()			-- Барабаны
	--TMR       = GetStatusTMR()	-- Таймера
end

Core.onTimer(3, 1,get_ust,true, true )
Core.waitEvents();