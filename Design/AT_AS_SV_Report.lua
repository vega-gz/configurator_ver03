
local function dump_fromDate(arg)
	local prefix = arg[1]		-- Префикс имени файла
    local dt = Core[arg[2]]		-- Буфер даты выгрузки [s]
	local depth = Core[arg[3]]	-- Глубина выгрузки [s]
	local interval = arg[4]		-- Интервал запроса данных [s]
	local dataSignals = arg[5]	-- Массив сигналов
    local Sel_Event =arg[6]		-- Выгрузка событий
    local Sel_Trend =arg[7]		-- Выгрузка тренда
    local front_start =arg[8]	-- Кнопка

 if Core[front_start]==true then 
	-- Получаем текущее время.
	local t = os.date("*t", dt);

 if Core[Sel_Event] == true then
	local fileName = "D:/REPORTS/Archive/"..os.date("_%Y%m%d_%H%M%S_", os.time())..prefix.."_EVENTS"..os.date("_%Y%m%d_%H%M%S.log", dt)
	-- Открываем файл.
	local file = io.open(fileName, "w+");
	Core.addLogMsg("Оpen file Event");
	if file==nil then return end
	-- Запрашиваем список событий от текущего времени и depth секунд назад.
	local events = Core.getEvents(dt-depth, dt);
	
    -- Сохраняем события в файл.
--	file:write("------------------------------------------------------------\n");
--	file:write("Архив событий\n");	
--	file:write("------------------------------------------------------------\n");
	for _,event in ipairs(events) do
		local date = os.date("%Y.%m.%d %H:%M:%S.%3N", event.dt);
		local msg = string.format("%s\t%s", date, event.msg);
        local state = string.format("%s", event.state); 
        local source = string.format("%s", event.source);
        if state == '769' then state = "Появление" else state = "Пропадание" end  -- 769? 
		file:write(msg..'\t'..state..'\t'..source..'\n');
	end
	file:close();
	file = nil;
	Core.addLogMsg("Close file Event");
  Core[Sel_Event] =false
 end

 if Core[Sel_Trend] == true then
  	dofile('decode_Ansi_Utf8.lua')	-- Открываем файл для декодирования
 	dofile('Trend.lua') 			-- Открываем таблицу
	fileName = "D:/REPORTS/Archive/"..os.date("_%Y%m%d_%H%M%S_", os.time())..prefix.."_TREND"..os.date("_%Y%m%d_%H%M%S.log", dt)
	file = io.open(fileName, "w+");	
	Core.addLogMsg("Оpen file Trend");
	if file==nil then return end
	-- Сохраняем значения сигналов в файл.
--	file:write("------------------------------------------------------------\n");
--	file:write("Архив сигналов \n");		
	local msg = "Date&time\t";
	for i=1, #SignalTrend  do
     if SignalTrend[i].Comment ~='' then
        name = AnsiToUtf8(tostring(SignalTrend[i].Comment))
        else 
        name = AnsiToUtf8(tostring(SignalTrend[i].Name))
     end
		msg = msg..name.."\t";
	end
--	file:write("------------------------------------------------------------\n");
	file:write(msg.."\n");
	for t = dt - depth, dt, interval do
		msg = os.date("%Y.%m.%d %H:%M:%S.%3N", t);
		for k=1, #SignalTrend do
			if SignalTrend[k].Name ~= nil then
				name = tostring(SignalTrend[k].Name)
				local arch = Core(t)[name];
				if type(arch) == nil then 
					Core.addLogMsg("Signal "..name.." not found");
					SignalTrend[k].Name= nil
				else 
					if arch.res==0 then
						msg = msg.."\t"..tostring(arch.val);
					else
						msg = msg.."\t";
					end
				end
			end
		end 
		file:write(msg.."\n");
	end
	file:close();
	Core.addLogMsg("Close file Trend");
	file = nil;
 Core[Sel_Trend]=false
 end
 end
 Core[front_start]=false
end


--– Создаём пустую таблицу.    
local function ReadDate(arg)
 local Set_bufer_Name = arg[1]
 local Set_bufer_prefName = arg[2]
 local ResReadDate = arg[3]
 local t = {}
 local DateTime 
 -- Заполняем поля таблицы.    
 t.year = Core[Set_bufer_prefName..'.'..Set_bufer_Name[1]]   
 t.month =Core[Set_bufer_prefName..'.'..Set_bufer_Name[2]]    
 t.day =  Core[Set_bufer_prefName..'.'..Set_bufer_Name[3]]   
 t.hour = Core[Set_bufer_prefName..'.'..Set_bufer_Name[4]]    
 t.min =  Core[Set_bufer_prefName..'.'..Set_bufer_Name[5]]     
 t.sec =  Core[Set_bufer_prefName..'.'..Set_bufer_Name[6]]    
 t.usec = Core[Set_bufer_prefName..'.'..Set_bufer_Name[7]] 
 if os.time(t)  < 0 or  os.time(t) + os.tz() > os.time() then Core[ResReadDate]= os.time() else Core[ResReadDate] = os.time(t)  + os.tz() end
end


Core.onExtChange(
				{"Arc_exp.Set_year", "Arc_exp.Set_month", "Arc_exp.Set_day", "Arc_exp.Set_hour", "Arc_exp.Set_min", "Arc_exp.Set_sec", "Arc_exp.Set_usec" },
				ReadDate,{{"Set_year", "Set_month", "Set_day", "Set_hour", "Set_min", "Set_sec", "Set_usec"},"Arc_exp","Arc_exp.Bufer_from_date"}
)


-- Регистрируем обработчик события изменения сигналов.
Core.onExtChange(
				{"Arc_exp.BTN_Make_file_from_date" },	-- Перечень имён сигналов, при изменении которых выполнять вызов функции dump.
				dump_fromDate,							-- Вызываемая функция
				{
					"User",					  			-- Префикс имени файла
                    "Arc_exp.Bufer_from_date",			-- Буфер даты выгрузки [s]
					"Arc_exp.Set_interval",				-- Глубина выгрузки [s]
					0.25,								-- Интервал запроса данных [s]
					MassAlarmValues,					-- Массив сигналов
					"Arc_exp.Select_Event",				-- Выгрузка событий 
					"Arc_exp.Select_Trend",				-- Выгрузка тренда
					"Arc_exp.BTN_Make_file_from_date"	-- Кнопка
				}
)


-------------------------------------------------------------------------------
-- Обработка рапортов
-------------------------------------------------------------------------------
local step;
local tmpTime;

local tmpTimePusk;
local FlacT_Pusk = false;
local Flac_Pusk = false;

local tmpTimeNO;
local FlacT_NO = false;
local Flac_NO = false;

local tmpTimeAO;
local FlacT_AO = false;
local Flac_AO = false;



-- Цикл частотой 1 сек.
while true do
	tmpTime = os.clock()

	--*** Пуск *** 1 мин до пуска и 10 мин после
	if Core.Trend_us.Start and Core.Trend_us.is_flame and FlacT_Pusk == false and Flac_Pusk == false then
		tmpTimePusk = tmpTime
		FlacT_Pusk = true
	end

	if Flac_Pusk == false and FlacT_Pusk == true and (Core.Trend_us.AOss or Core.Trend_us.AObs or Core.Trend_us.NOss or Core.Trend_us.NObs or (tmpTime - tmpTimePusk) > 600.0) then
		Flac_Pusk = true
		FlacT_Pusk = false

		Core["Arc_exp.Bufer_from_date"] = os.time()		-- Буфер даты выгрузки [s]
		Core["Arc_exp.Set_interval"] = 660				-- Глубина выгрузки [s]
		Core["Arc_exp.Select_Event"] = true				-- Выгрузка событий 
		Core["Arc_exp.Select_Trend"] = true				-- Выгрузка тренда
		Core["Arc_exp.BTN_Make_file_from_date"] = true	-- Кнопка

		dump_fromDate									-- Вызываемая функция
		{
			"Start",		    						-- Префикс имени файла
			"Arc_exp.Bufer_from_date",					-- Буфер даты выгрузки [s]
			"Arc_exp.Set_interval",						-- Глубина выгрузки [s]
			0.25,										-- Интервал запроса данных [s]
			MassAlarmValues,							-- Массив сигналов
			"Arc_exp.Select_Event",						-- Выгрузка событий 
			"Arc_exp.Select_Trend",						-- Выгрузка тренда
			"Arc_exp.BTN_Make_file_from_date"
		}
	end

	if not (Core.Trend_us.Start and Core.Trend_us.is_flame) and Flac_Pusk == true then 
		FlacT_Pusk = false
		Flac_Pusk = false
	end


	--*** НО *** 1 мин до НО и 10 мин после
	if (Core.Trend_us.NOss or Core.Trend_us.NObs) and FlacT_NO == false and Flac_NO == false then 
		tmpTimeNO = tmpTime
		FlacT_NO = true
	end

	if Flac_NO == false and FlacT_NO == true and (Core.Trend_us.AOss or Core.Trend_us.AObs or (tmpTime - tmpTimeNO) > 600.0) then
		Flac_NO = true
		FlacT_NO = false
		Core["Arc_exp.Bufer_from_date"] = os.time()		-- Буфер даты выгрузки [s]
		Core["Arc_exp.Set_interval"] = 660				-- Глубина выгрузки [s]
		Core["Arc_exp.Select_Event"] = true				-- Выгрузка событий 
		Core["Arc_exp.Select_Trend"] = true				-- Выгрузка тренда
		Core["Arc_exp.BTN_Make_file_from_date"] = true	-- Кнопка

		dump_fromDate									-- Вызываемая функция
		{
			"NO",		    							-- Префикс имени файла
			"Arc_exp.Bufer_from_date",					-- Буфер даты выгрузки [s]
			"Arc_exp.Set_interval",						-- Глубина выгрузки [s]
			0.25,										-- Интервал запроса данных [s]
			MassAlarmValues,							-- Массив сигналов
			"Arc_exp.Select_Event",						-- Выгрузка событий 
			"Arc_exp.Select_Trend",						-- Выгрузка тренда
			"Arc_exp.BTN_Make_file_from_date"
		}
	end

	if not (Core.Trend_us.NOss or Core.Trend_us.NObs) and Flac_NO == true then
		FlacT_NO = false
		Flac_NO = false
	end


	--*** АО *** 10 мин до АО и 1 мин после
	if (Core.Trend_us.AOss or Core.Trend_us.AObs) and FlacT_AO == false and Flac_AO == false then
		tmpTimeAO = tmpTime
		FlacT_AO = true
	end

	if Flac_AO == false and FlacT_AO == true and (tmpTime - tmpTimeAO) > 60.0 then
		Flac_AO = true
		FlacT_AO = false

		Core["Arc_exp.Bufer_from_date"] = os.time()		-- Буфер даты выгрузки [s]
		Core["Arc_exp.Set_interval"] = 660				-- Глубина выгрузки [s]
		Core["Arc_exp.Select_Event"] = true				-- Выгрузка событий 
		Core["Arc_exp.Select_Trend"] = true				-- Выгрузка тренда
		Core["Arc_exp.BTN_Make_file_from_date"] = true	-- Кнопка

		dump_fromDate									-- Вызываемая функция
		{
			"AO",		    							-- Префикс имени файла
			"Arc_exp.Bufer_from_date",					-- Буфер даты выгрузки [s]
			"Arc_exp.Set_interval",						-- Глубина выгрузки [s]
			0.25,										-- Интервал запроса данных [s]
			MassAlarmValues,							-- Массив сигналов
			"Arc_exp.Select_Event",						-- Выгрузка событий 
			"Arc_exp.Select_Trend",						-- Выгрузка тренда
			"Arc_exp.BTN_Make_file_from_date"
		}
	end

	if not (Core.Trend_us.AOss or Core.Trend_us.AObs) and Flac_AO == true then
		FlacT_AO = false
		Flac_AO = false
	end


--	local time = os.time();
	local t = os.date("*t", dt);

	-- Смена начинается в 6:00. Первая запись 7:00МСК (9:00УФА), далее +2часа
	-- На АРМе Московское время (+3)
	-- В сменной ведомости отчет от начала смены (7:00) +0ч, +2ч, +4ч,...., +22ч
	if (t.hour >= 0 - os.tz()/3600) and t.hour < 6 then
		Core.Smen_today = os.today() + os.tz() + (7 - 24)*3600	--7:00 Дата и время начала смены (первая точка) (день назад)
	else
		Core.Smen_today = os.today() + os.tz() + 7*3600			--7:00 Дата и время начала смены (первая точка)
	end

	--Смена 1 с 8:00 по 20:00
	if t.hour == 7 or t.hour == 9 or t.hour == 11 or t.hour == 13 or t.hour == 15 or t.hour == 17 then
		if step1 == 0 then
			Core.Make_SV1 = true
			--Core.Make_SUT = true
			step1 = 1
		end
	else
		step1 = 0
	end

	--Смена 2 с 20:00 по 8:00
	if t.hour == 19 or t.hour == 21 or t.hour == 23 or t.hour == 1 or t.hour == 3 or t.hour == 5 then
		if step2 == 0 then
			if t.hour == 5 then Core.Make_SV2 = true else Core.Make_SV_tmp = true end
			if t.hour == 23 then Core.Make_SUT = true end								-- Суточная создается раз в сутки
			step2 = 1
		end
	else
		step2 = 0
	end

os.sleep(1)
end