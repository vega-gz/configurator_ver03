------------------ Сохранение данных в файле ------------------------------------ Steam 
------------ проверка наличия файла ---------------------------------------------
local isfile = function(flname)
	local fl = assert(io.open(flname, "r"), "Файл "..flname.." не найден")
	fl:close()
end

------------------------- запись данных в файл ----------------------------------
local CopyData = function(cx) 
	-- [1] тип абонента
	-- [2] абонент
	-- [3] префикс структуры
	local fl_data = io.open(cx[2].."_Data_"..cx[3]..".tun",'w')
	for _, nm in ipairs(Tag[cx[1].."_"..cx[3] ]) do
		fl_data:write(tostring(Core[cx[2].."_"..cx[3].."."..nm]).."\n")
	end
	fl_data:close()
end

-------------------------- чтение данных из файла -------------------------------
local ReadData = function(cx)
	local filename = cx[2].."_Data_"..cx[3]..".tun"
	local fl_data = io.open(filename, 'r')
	if fl_data == nil then 
		Core[cx[2].."_Save.file"] = filename 
		return -- нет такого файла
	end 
	for _, nm in ipairs(Tag[cx[1].."_"..cx[3] ]) do
		local value = io.read()
		if value == nil then break end -- достигнут конец файла
		Core[cx[2].."_"..cx[3].."."..nm] = value 
	end
	fl_data:close()
end

---------------------------- запись характеристик в файл --------------------------
local CopyCurve = function(cx) 
	local fl_data = io.open(cx[2].."_Data_"..cx[3]..".tun",'w')
	for _, nm in ipairs(Tag[cx[1].."_"..cx[3] ]) do
		local CurveTagName = cx[2].."_"..cx[3].."."..nm
		for i= 0, Core[CurveTagName].N- 1 do
			fl_data:write(tostring(Core[CurveTagName].s[i].x).."\n")		
			fl_data:write(tostring(Core[CurveTagName].s[i].fx).."\n")
		end
	end	
	fl_data:close()
end

---------------------------------- чтение ххарактеристики из файла --------------------
local ReadCurve = function(cx)
	local filename = cx[2].."_Data_"..cx[3]..".tun" 	
	local fl_data = io.open(filename, 'r')
	if fl_data == nil then 
		Core[cx[2].."_Save.file"] = filename 
		return -- нет такого файла
	end
	for _, nm in ipairs(Tag[cx[1].."_"..cx[3] ]) do
		local CurveTagName = cx[2].."_"..cx[3].."."..nm
		for i= 0, Core[CurveTagName].N- 1 do
			for j= 1, 2 do
				local value = io.read()
				if value == nil then -- достигнут конец файла
					fl_data:close() 
					return
				end 
				if j == 1 then Core[CurveTagName].s[i].x = value
						  else Core[CurveTagName].s[i].fx = value
				end
			end
		end
	end 
	fl_data:close()
end

-- ===============================================================================================================
Tag={}
	isfile('GPA_Tune_FR.lua')
	isfile('GPA_Tune.lua')
	isfile('GPA_Curve_FR.lua')
	isfile('GPA_RSR.lua')
	dofile('GPA_Tune_FR.lua') 	-- объявление таблицы имен настроек ТР
	dofile('GPA_Tune.lua') 	-- объявление таблицы имен настроек ГПА
	dofile('GPA_Curve_FR.lua') 	-- объявление таблицы имен кривых ТР
	dofile('GPA_RSR.lua') 	-- объявление таблицы имен ресурсов

-- Файлы данных *.tun размещаются в директории Сонаты !!!!, а не в директории проекта (Design) 

Core.onExtChange({"GPA4_Save.ReadData"}, ReadData, {"GPA", "GPA4", "Tune_FR"}) -- чтение настроек ТР ГПА4 из файла
Core.onExtChange({"GPA4_Save.CopyData"}, CopyData, {"GPA", "GPA4", "Tune_FR"}) -- запись настроек ТР ГПА4 в файл
Core.onExtChange({"GPA5_Save.ReadData"}, ReadData, {"GPA", "GPA5", "Tune_FR"}) -- чтение настроек ТР ГПА5 из файла
Core.onExtChange({"GPA5_Save.CopyData"}, CopyData, {"GPA", "GPA5", "Tune_FR"}) -- запись настроек ТР ГПА5 в файл
Core.onExtChange({"GPA6_Save.ReadData"}, ReadData, {"GPA", "GPA6", "Tune_FR"}) -- чтение настроек ТР ГПА6 из файла
Core.onExtChange({"GPA6_Save.CopyData"}, CopyData, {"GPA", "GPA6", "Tune_FR"}) -- запись настроек ТР ГПА6 в файл
Core.onExtChange({"GPA4_Save.ReadData"}, ReadData, {"GPA", "GPA4", "Tune"}) -- чтение настроек ГПА4 из файла
Core.onExtChange({"GPA4_Save.CopyData"}, CopyData, {"GPA", "GPA4", "Tune"}) -- запись настроек ГПА4 в файл
Core.onExtChange({"GPA5_Save.ReadData"}, ReadData, {"GPA", "GPA5", "Tune"}) -- чтение настроек ГПА5 из файла
Core.onExtChange({"GPA5_Save.CopyData"}, CopyData, {"GPA", "GPA5", "Tune"}) -- запись настроек ГПА5 в файл
Core.onExtChange({"GPA6_Save.ReadData"}, ReadData, {"GPA", "GPA6", "Tune"}) -- чтение настроек ГПА6 из файла
Core.onExtChange({"GPA6_Save.CopyData"}, CopyData, {"GPA", "GPA6", "Tune"}) -- запись настроек ГПА6 в файл
Core.onExtChange({"GPA4_Save.ReadData"}, ReadCurve, {"GPA", "GPA4", "Curve_FR"}) -- чтение настроек ТР ГПА4 из файл
Core.onExtChange({"GPA4_Save.CopyData"}, CopyCurve, {"GPA", "GPA4", "Curve_FR"}) -- запись настроек ТР ГПА4 в файл
Core.onExtChange({"GPA5_Save.ReadData"}, ReadCurve, {"GPA", "GPA5", "Curve_FR"}) -- чтение настроек ТР ГПА5 из файла
Core.onExtChange({"GPA5_Save.CopyData"}, CopyCurve, {"GPA", "GPA5", "Curve_FR"}) -- запись настроек ТР ГПА5 в файл
Core.onExtChange({"GPA6_Save.ReadData"}, ReadCurve, {"GPA", "GPA6", "Curve_FR"}) -- чтение настроек ТР ГПА6 из файла
Core.onExtChange({"GPA6_Save.CopyData"}, CopyCurve, {"GPA", "GPA6", "Curve_FR"}) -- запись настроек ТР ГПА6 в файл

Core.onExtChange({"GPA4_SaveRes.CopyData"}, CopyData, {"GPA", "GPA4", "RSR"}) -- запись счетчиков ГПА 4
Core.onExtChange({"GPA4_SaveRes.ReadData"}, ReadData, {"GPA", "GPA4", "RSR"}) -- чтение счетчиков ГПА4 из файла
Core.onExtChange({"GPA5_SaveRes.CopyData"}, CopyData, {"GPA", "GPA5", "RSR"}) -- запись счетчиков ГПА 5
Core.onExtChange({"GPA5_SaveRes.ReadData"}, ReadData, {"GPA", "GPA5", "RSR"}) -- чтение счетчиков ГПА5 из файла
Core.onExtChange({"GPA6_SaveRes.CopyData"}, CopyData, {"GPA", "GPA6", "RSR"}) -- запись счетчиков ГПА 6
Core.onExtChange({"GPA6_SaveRes.ReadData"}, ReadData, {"GPA", "GPA6", "RSR"}) -- чтение счетчиков ГПА6 из файла

Core.waitEvents()
