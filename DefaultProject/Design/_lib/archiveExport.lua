--[[ Пример вызова

require("./_lib/archiveExport")	--Модуль выгрузки архивов

Core.onExtChange(	{"ArcExp.BTN_UploadArchive"},            -- Кнопка по изменению которой вызывается функция
						archiveExport,                       -- Функция
							{"User",                         -- Инициатор вызова функции (префикс имени файла)
							0.25,                            -- Интервал запроса данных [s]
							"ArcExp.Bufer_from_date",        -- Буфер даты выгрузки [s]
							"ArcExp.Set_interval",           -- Глубина выгрузки [s]
							"ArcExp.Select_Event",           -- Кнопка выбора событий
							"ArcExp.Select_Trend",           -- Кнопка выбора тренда
							"ArcExp.BTN_UploadArchive",      -- Кнопка Начала выгрузки
							"ArcExp.BTN_UploadArchiveStop",  -- Кнопка Останова выгрузки						
							"ArcExp.DateTime",               -- Время от которого будет выгрузка (в глубь)
							"Trend.lua",                     -- Путь до базы с сигналами которые выгружаем (для Трендов)
							"D:/REPORTS/Archive/"            -- Путь выгрузки архивов
							}
				)]]

function archiveExport(arg)
	-- Распределяем, вытягиваем значения
	local prefix                             = arg[1]                 -- Префикс имени файла
	local interval                           = arg[2]                 -- Интервал запроса данных [s]
	local result, dt, utc, flags             = Core.getSignal(arg[3]) -- Буфер даты выгрузки [s]
	local result, depth, utc, flags          = Core.getSignal(arg[4]) -- Глубина выгрузки [s]
	local result, Sel_Event, utc, flags      = Core.getSignal(arg[5]) -- Выгрузка событий
	local result, Sel_Trend, utc, flags      = Core.getSignal(arg[6]) -- Выгрузка тренда
	local result, function_start, utc, flags = Core.getSignal(arg[7]) -- Кнопка начала выгрузки
--	local result, function_stop, utc, flags  = Core.getSignal(arg[8]) -- Кнопка остановки выгрузки
	local result, DateTime, utc, flags       = Core.getSignal(arg[9]) -- Выгрузка событий
	local baseSignal                         = arg[10]                -- Файл с базой сигналов
	local path_unloading                     = arg[11]                -- путь выгрузки архивов
---------------------------------------------------------------------------------------------------

	-- Функция для остановки процесса выгрузки
	local function stopExport(file, name, cat)
		Core.addLogMsg("Export. "..prefix.." - Unloading terminated at the initiative of the user");
		file:close();
		os.remove(name) -- в Linux системах может не рпаботать
		Core.addLogMsg("Export. File "..cat.." deleted");
		Core[arg[5]] = false
		Core[arg[6]] = false
		Core[arg[7]] = false
		Core[arg[8]] = false
		Core.addLogMsg('Export. '..prefix..' - Flags reset');
		return true
	end

	-- Функция проверки фала
	function openFile(file)
		dofile(file)
	end
---------------------------------------------------------------------------------------------------

	-- Обрабатываем дату
	if DateTime ~= '' then 
		local DateTimeTable = {}
			for s in string.gmatch(DateTime, "[^,]+") do
			   table.insert(DateTimeTable, s)
			end
		--– Создаём пустую таблицу.
		 local t = {}
			 t.year  = DateTimeTable[1]
			 t.month = DateTimeTable[2]
			 t.day   = DateTimeTable[3]
			 t.hour  = DateTimeTable[4]
			 t.min   = DateTimeTable[5]
			 t.sec   = DateTimeTable[6]
			 t.usec  = DateTimeTable[7]
		if os.time(t)  < 0 or  os.time(t) + os.tz() > os.time() then dt= os.time() else dt = os.time(t)  + os.tz() end 
	end

	-- Сработала команда Выгрузить архив
	Core.addLogMsg('Export. '..prefix..' - Unloading started')
	if function_start==true then 
		-- Получаем текущее время.
		local t = os.date("*t", dt);

		-- Если выбрана выгрузка Событий
		 if Sel_Event == true then
			local fileName = path_unloading..os.date("_%Y%m%d_%H%M%S_", os.time())..prefix.."_EVENTS"..os.date("_%Y%m%d_%H%M%S.log", dt)
			-- Открываем файл для записи
			local file = io.open(fileName, "w+");
			Core.addLogMsg('Export. '..prefix..' - Event file opened');
			if file==nil then return end 
			-- Запрашиваем список событий от текущего времени и depth секунд назад.
			local events = Core.getEvents(dt-depth, dt);
			
			-- Сохраняем события в файл.
			--	file:write("------------------------------------------------------------\n");
			--	file:write("Архив событий\n");	
			--	file:write("------------------------------------------------------------\n");
				for _,event in ipairs(events) do
					local result, function_stop, utc, flags  = Core.getSignal(arg[8]) -- Кнопка остановки выгрузки
					if function_stop == true then breakExport = stopExport(file, fileName, 'Events') end
						if not breakExport then
							local date = os.date("%Y.%m.%d %H:%M:%S.%3N", event.dt);
							local msg = string.format("%s\t%s", date, event.msg);
					        local state = string.format("%s", event.state); 
					        local source = string.format("%s", event.source);
					        if state == '1' then state = "Появление" else state = "Пропадание" end
							file:write(msg..'\t'..state..'\t'..source..'\n');
						else
							return
						end
					end
			file:close();
			file = nil;
			Core.addLogMsg('Export. '..prefix..' - Event file closed');
			--Сбрасываем выбор Событий
			Core[arg[5]] = false
			Core.addLogMsg('Export. '..prefix..' - Event upload completed')
		 end

		-- Если выбрана выгрузка Трендов
		if Sel_Trend == true then
			--dofile('decode_Ansi_Utf8.lua')	 -- Открываем файл для декодирования
			--dofile(baseSignal) 			     -- Открываем таблицу
			local success, err = pcall(openFile, baseSignal)
			if success == false then 
				Core.addLogMsg('Export. '..prefix..' - Trends not open file: '..err);
 				Core[arg[6]] = false
 	 			Core[arg[7]] = false
				return 
			end
			fileName = path_unloading..os.date("_%Y%m%d_%H%M%S_", os.time())..prefix.."_TREND"..os.date("_%Y%m%d_%H%M%S.log", dt)
			file = io.open(fileName, "w+");	
			Core.addLogMsg('Export. '..prefix..' - Trend file opened');
			if file==nil then return end
		-- Сохраняем значения сигналов в файл.
		 --	file:write("------------------------------------------------------------\n");
		 --	file:write("Архив сигналов \n");		
			local msg = "Date&time\t";
				for i=1, #SignalTrend  do
					local result, function_stop, utc, flags  = Core.getSignal(arg[8]) -- Кнопка остановки выгрузки
					if function_stop == true then breakExport = stopExport(file, fileName, 'Trend') end
						if not breakExport then
						    if SignalTrend[i].Comment ~='' then
						        name = AnsiToUtf8(tostring(SignalTrend[i].Comment))
						    else 
						        name = AnsiToUtf8(tostring(SignalTrend[i].Name))
						    end
							msg = msg..name.."\t";
						else
							return
						end
				end
		 --	file:write("------------------------------------------------------------\n");
			file:write(msg.."\n");
			for t = dt - depth, dt, interval do
				msg = os.date("%Y.%m.%d %H:%M:%S.%3N", t);
				for k=1, #SignalTrend do
					if function_stop ~= true then 
						if SignalTrend[k].Name ~= nil then
							name = tostring(SignalTrend[k].Name)
							local arch = Core(t)[name];
							if type(arch) == nil then 
								Core.addLogMsg('Export. '..prefix..' - Signal '..name..' not found');
								SignalTrend[k].Name= nil
							else 
								if arch.res==0 then
									msg = msg.."\t"..tostring(arch.val);
								else
									msg = msg.."\t";
								end
							end
						end
					else return 
					end
				end 
				file:write(msg.."\n");
			end
			file:close();
			Core.addLogMsg('Export. '..prefix..' - Closed trend file');
			file = nil;
			--Сбрасываем выбор Трендов
			Core[arg[6]] = false
			Core.addLogMsg('Export. '..prefix..' - Trend upload completed')
		end
	end
	--Сбрасываем Кнопку Выгрузка архива
 	Core[arg[7]] = false
	Core.addLogMsg('Export. '..prefix..' - Upload completed')
end