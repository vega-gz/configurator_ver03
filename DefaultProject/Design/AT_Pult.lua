------------ проверка наличия файла --------------------------------------------- Steam
local isfile = function(flname)
	local fl = assert(io.open(flname, "r"), "Файл "..flname.." не найден")
	fl:close()
end
-- пара из таблицы t
local getPair = function (t, i) return t[2 *i-1], t[2 *i] end 

------------------------ Режимные сообщения --------------------------------------- Steam
local function ModeMessages(cx)
-- cx{3] -> red пороговый номер индекса режима в массиве, до которого поле пейждера надо окрашивать в красный цвет (АО, ВО) включительно
-- cx{4] -> yel пороговый номер индекса режима в массиве, до которого поле пейджера надо окрашивать в желтый цвет (Ремонт, НО ...) 
	-- если нет связи
	if Core[cx[2].."_AbStatus.NoLink_CPU"] then 
		Core[cx[2].."_AbStatus.ModeMessage"] = "Нет связи" 
		Core[cx[2].."_AbStatus.ModeColor"] = 0
		return 
	end
	local j = 0
	-- вывод сообщения
	Core[cx[2].."_AbStatus.ModeMessage"] = ""
	for key, id in ipairs(Tag[cx[1].."_mde"]) do 
 		if Core[cx[2]..id] then 
			Core[cx[2].."_AbStatus.ModeMessage"] = Core.getSignalComment(cx[2]..id)
			j = key 
			break
 		end 
	end 	
	-- Выбор цвета
 	if j <= cx[3] then Core[cx[2].."_AbStatus.ModeColor"] = 2  -- крас (авария)
		elseif j <= cx[4] then Core[cx[2].."_AbStatus.ModeColor"] = 3 -- жел (НО, ремонт)
		else Core[cx[2].."_AbStatus.ModeColor"] = 1 -- зел (норма)
 	end
end

---------------------------- Первопричина  ---------------------------------------- Steam
local function GetFirstOut(cx)
 if Core[cx[2].."_FirstOutIndex"] == 0 then Core[cx[2].."_AbStatus.FirstOut"] = "" return end -- нет аварии, нет месседжа
 local ID, AO = getPair(Tag[cx[1].."_crs"], Core[cx[2].."_FirstOutIndex"])
-- формирование строки первопричины 
 Core[cx[2].."_AbStatus.FirstOut"] = "Первопричина ".. AO .. " - " .. Core.getSignalComment(cx[2]..ID)
end

------------------------- Техгологические сообщения ------------------------------- Steam
local function TechMessages(typeAb, ab)
	-- если нет связи
	if Core[ab.."_AbStatus.NoLink_CPU"] then Core[ab.."_AbStatus.TchMessage"] = "" return end
	local sumMsg = {} --таблица очереди техн. сообщений
	for _, id in ipairs(Tag[typeAb.."_tch"]) do 
		if Core[ab..id] then  table.insert(sumMsg, Core.getSignalComment(ab..id)) end -- записываем активное сообщение в очередь
	end
	Core[ab.."_AbStatus.TchMessage"] = table.concat(sumMsg, "\n")
end

-------------------------------- Технологические задержки --------------------------- Steam
local function PrintDelay(typeAb, ab)
	local sumMsg = {} --таблица очереди таймеров
	for _, id in ipairs(Tag[typeAb.."_dlt"]) do
		local ct = Core[ab.."_TMR."..id]
		local dt = Core[ab.."_DLT."..id]
		if ct <= dt and ct ~= 0 then	
			local elapse = string.gsub(tostring(dt - ct), "(%p)(.+)", "") -- остаток времени
			table.insert(sumMsg, Core.getSignalComment(ab.."_TMR."..id).." - "..elapse) --  запись строки с таймером в очередь
		end
	end
	Core[ab.."_AbStatus.TimerDelay"] = table.concat(sumMsg, "\n") 
end

--------------------------- Сброс лестницы ------------------------------Steam
local function LadReset(lad)
	Core[lad[1] ].Qnty = 0
	Core[lad[1] ].CurStep = 0
	for i= 0, 27 do Core[lad[1] ].StepText[i] ="" end
end

------------------------ формирование лестницы ---------------------------Steam
local function LadPrint(cx)
	if Core[cx[3] ] < 1 then return end
	if Core[cx[1].."_LAD.Qnty"] == 0 then  -- заполнение полей лестницы именами 
  		Core[cx[1].."_LAD.Qnty"] = #Drum[cx[2] ] -- общее число шагов
		for i, v in ipairs(Drum[cx[2] ]) do Core[cx[1].."_LAD.StepText"][i-1] = v end
  	end	
	 -- вычисление  текущего шага
    local curstep = 0
    for i=3, #cx do curstep = curstep + Core[cx[i] ] end
    Core[cx[1].."_LAD.CurStep"] = curstep
end

--------------------- Звуковая сигнализация от всех абонентов -------------------- Steam
local function AllSound()
	Core.SoundCrash = Core.KC_Sound.crash or   -- Аварийная сирена
					  Core.UVI_Sound.crash or
					  Core.UPA_Sound.crash or	
					  Core.TSK_Sound.crash or
					  Core.NMS_Sound.crash or
					  Core.GPA4_Sound.crash or
					  Core.GPA5_Sound.crash or
					  Core.GPA6_Sound.crash
	
	Core.SoundAlarm = (Core.KC_Sound.alarm or  -- Предупредительный зумер
					  Core.UVI_Sound.alarm or
					  Core.UPA_Sound.alarm or	
					  Core.TSK_Sound.alarm or
					  Core.NMS_Sound.alarm or
					  Core.GPA4_Sound.alarm or
					  Core.GPA5_Sound.alarm or
					  Core.GPA6_Sound.alarm
					  ) and not Core.SoundCrash				 
end

-- ===============================================================================================
Tag = {} -- объявление таблицы глобальных имен
Drum = {} --объявление глобальной таблицы барабанов
-- Проверка наличия файлов таблиц ------------------------------ Steam
	isfile('GPA_Mode.lua')
	isfile('KC_Mode.lua')
	isfile('UPA_Mode.lua')
	isfile('UVI_Mode.lua')
	isfile('NMS_Mode.lua')
	isfile('TSK_Mode.lua')
	isfile('GPA_FirstOut.lua')
	isfile('KC_FirstOut.lua')
	isfile('GPA_TechMessages.lua')
	isfile('GPA_Delay.lua')
	isfile('GPA_Drums.lua') 

	dofile('GPA_Mode.lua') 	-- объявление таблицы имен режимов ГПА
	dofile('KC_Mode.lua') -- объявление таблицы имен режимов КЦ 
	dofile('UPA_Mode.lua') -- объявление таблицы имен режимов УПА
	dofile('UVI_Mode.lua')  -- объявление таблицы имен режимов УВИ
	dofile('NMS_Mode.lua')  -- объявление таблицы имен режимов НМС
	dofile('TSK_Mode.lua')  -- объявление таблицы имен режимов ТСК
	dofile('GPA_FirstOut.lua') -- объявление таблицы имен тревог для ГПА
	dofile('KC_FirstOut.lua') -- объявление таблицы имен тревог для КЦ
	dofile('GPA_TechMessages.lua') -- объявление таблицы имен тс для ГПА
	dofile('GPA_Delay.lua') -- объявление таблицы имен тс для ГПА
	dofile('GPA_Drums.lua') -- объявление таблицы шагов барабанов для ГПА

-- ============================ Вызовы функций =====================================================
-- вызов ф-ции при запуске приложения (разово)
ModeMessages({"KC", "KC", 5, 6})
 ModeMessages({"UPA", "UPA", 2, 2})
 ModeMessages({"UVI", "UVI", 2, 2})
 ModeMessages({"NMS", "NMS", 2, 2})
 ModeMessages({"TSK", "TSK", 2, 2})
 ModeMessages({"GPA", "GPA4", 6, 10})
 ModeMessages({"GPA", "GPA5", 6, 10})
 ModeMessages({"GPA", "GPA6", 6, 10})

-- Синхронный вызов функций по времени
local function Call_synchro()
	TechMessages("GPA", "GPA4")  -- Технологические сообщения для ГПА4
	PrintDelay("GPA", "GPA4")  -- Задержки для ГПА4
	TechMessages("GPA", "GPA5")  -- Технологические сообщения для ГПА5
	PrintDelay("GPA", "GPA5")  -- Задержки для ГПА5
	TechMessages("GPA", "GPA6")  -- Технологические сообщения для ГПА6
	PrintDelay("GPA", "GPA6")  -- Задержки для ГПА6
	AllSound()   -- Звук, steam
end
Core.onTimer(3, 1,Call_synchro)

-- вызов ф-ции по событию
Core.onExtChange({"KC_Mode.Num", "KC_AbStatus.NoLink_CPU"}, ModeMessages, {"KC", "KC", 5, 6})
Core.onExtChange({"UPA_Mode.Num", "UPA_AbStatus.NoLink_CPU"}, ModeMessages, {"UPA", "UPA", 2, 2})
Core.onExtChange({"UVI_Mode.Num", "UVI_AbStatus.NoLink_CPU"}, ModeMessages, {"UVI", "UVI", 2, 2})
Core.onExtChange({"NMS_Mode.Num", "NMS_AbStatus.NoLink_CPU"}, ModeMessages, {"NMS", "NMS", 2, 2})
Core.onExtChange({"TSK_Mode.Num", "TSK_AbStatus.NoLink_CPU"}, ModeMessages, {"TSK", "TSK", 2, 2})
Core.onExtChange({"GPA4_Mode.Num", "GPA4_AbStatus.NoLink_CPU"}, ModeMessages, {"GPA", "GPA4", 6, 10})
Core.onExtChange({"GPA5_Mode.Num", "GPA5_AbStatus.NoLink_CPU"}, ModeMessages, {"GPA", "GPA5", 6, 10})
Core.onExtChange({"GPA6_Mode.Num", "GPA6_AbStatus.NoLink_CPU"}, ModeMessages, {"GPA", "GPA6", 6, 10})
Core.onExtChange({"KC_FirstOutIndex"}, GetFirstOut, {"KC", "KC"})
Core.onExtChange({"GPA4_FirstOutIndex"}, GetFirstOut, {"GPA", "GPA4"})
Core.onExtChange({"GPA5_FirstOutIndex"}, GetFirstOut, {"GPA", "GPA5"})
Core.onExtChange({"GPA6_FirstOutIndex"}, GetFirstOut, {"GPA", "GPA6"})

------------------------------- Лестницы алгоритмов -------------------------------------------------------------------------------------
-- очистка лестниц
Core.onExtChange({"GPA4_Mode.Num"}, LadReset, {"GPA4_LAD"}) 
Core.onExtChange({"GPA5_Mode.Num"}, LadReset, {"GPA5_LAD"})
Core.onExtChange({"GPA6_Mode.Num"}, LadReset, {"GPA6_LAD"})
-- Прокрутка
Core.onExtChange({"GPA4_DSN.Crank", "GPA4_DSN.StartCrank", "GPA4_DSN.StopCrank"}, LadPrint, {"GPA4", "GPA_Crank", "GPA4_DSN.Crank", "GPA4_DSN.StartCrank", "GPA4_DSN.StopCrank"})
Core.onExtChange({"GPA5_DSN.Crank", "GPA5_DSN.StartCrank", "GPA5_DSN.StopCrank"}, LadPrint, {"GPA5", "GPA_Crank", "GPA5_DSN.Crank", "GPA5_DSN.StartCrank", "GPA5_DSN.StopCrank"})
Core.onExtChange({"GPA6_DSN.Crank", "GPA6_DSN.StartCrank", "GPA6_DSN.StopCrank"}, LadPrint, {"GPA6", "GPA_Crank", "GPA6_DSN.Crank", "GPA6_DSN.StartCrank", "GPA6_DSN.StopCrank"})
--АПК без газа
Core.onExtChange({"GPA4_DSN.APKng", "GPA4_DSN.ZapK", "GPA4_DSN.StartD", "GPA4_DSN.StartCrank"}, LadPrint, {"GPA4", "GPA_APKng", "GPA4_DSN.APKng", "GPA4_DSN.ZapK", "GPA4_DSN.StartD", "GPA4_DSN.StartCrank"})
Core.onExtChange({"GPA5_DSN.APKng", "GPA5_DSN.ZapK", "GPA5_DSN.StartD", "GPA5_DSN.StartCrank"}, LadPrint, {"GPA5", "GPA_APKng", "GPA5_DSN.APKng", "GPA5_DSN.ZapK", "GPA5_DSN.StartD", "GPA5_DSN.StartCrank"})
Core.onExtChange({"GPA6_DSN.APKng", "GPA6_DSN.ZapK", "GPA6_DSN.StartD", "GPA6_DSN.StartCrank"}, LadPrint, {"GPA6", "GPA_APKng", "GPA6_DSN.APKng", "GPA6_DSN.ZapK", "GPA6_DSN.StartD", "GPA6_DSN.StartCrank"})
-- АПК с газом
Core.onExtChange({"GPA4_DSN.APKg", "GPA4_DSN.ZapK", "GPA4_DSN.StartD", "GPA4_DSN.StartCrank"}, LadPrint, {"GPA4", "GPA_APKg", "GPA4_DSN.APKg", "GPA4_DSN.ZapK", "GPA4_DSN.StartD", "GPA4_DSN.StartCrank"})
Core.onExtChange({"GPA5_DSN.APKg", "GPA5_DSN.ZapK", "GPA5_DSN.StartD", "GPA5_DSN.StartCrank"}, LadPrint, {"GPA5", "GPA_APKg", "GPA5_DSN.APKg", "GPA5_DSN.ZapK", "GPA5_DSN.StartD", "GPA5_DSN.StartCrank"})
Core.onExtChange({"GPA6_DSN.APKg", "GPA6_DSN.ZapK", "GPA6_DSN.StartD", "GPA6_DSN.StartCrank"}, LadPrint, {"GPA6", "GPA_APKg", "GPA6_DSN.APKg", "GPA6_DSN.ZapK", "GPA6_DSN.StartD", "GPA6_DSN.StartCrank"})
--АПМ без газа
Core.onExtChange({"GPA4_DSN.APMng", "GPA4_DSN.ZapK", "GPA4_DSN.StartD", "GPA4_DSN.StartCrank", "GPA4_DSN.Rp_KM"}, LadPrint, {"GPA4", "GPA_APMng", "GPA4_DSN.APMng", "GPA4_DSN.ZapK", "GPA4_DSN.StartD", "GPA4_DSN.StartCrank", "GPA4_DSN.Rp_KM"})
Core.onExtChange({"GPA5_DSN.APMng", "GPA5_DSN.ZapK", "GPA5_DSN.StartD", "GPA5_DSN.StartCrank", "GPA5_DSN.Rp_KM"}, LadPrint, {"GPA5", "GPA_APMng", "GPA5_DSN.APMng", "GPA5_DSN.ZapK", "GPA5_DSN.StartD", "GPA5_DSN.StartCrank", "GPA5_DSN.Rp_KM"})
Core.onExtChange({"GPA6_DSN.APMng", "GPA6_DSN.ZapK", "GPA6_DSN.StartD", "GPA6_DSN.StartCrank", "GPA6_DSN.Rp_KM"}, LadPrint, {"GPA6", "GPA_APMng", "GPA6_DSN.APMng", "GPA6_DSN.ZapK", "GPA6_DSN.StartD", "GPA6_DSN.StartCrank", "GPA6_DSN.Rp_KM"})
--АПМ без газа
Core.onExtChange({"GPA4_DSN.APMg", "GPA4_DSN.StartD", "GPA4_DSN.StartCrank", "GPA4_DSN.Rp_KM"}, LadPrint, {"GPA4", "GPA_APMg", "GPA4_DSN.APMg", "GPA4_DSN.StartD", "GPA4_DSN.StartCrank", "GPA4_DSN.Rp_KM"})
Core.onExtChange({"GPA5_DSN.APMg", "GPA5_DSN.StartD", "GPA5_DSN.StartCrank", "GPA5_DSN.Rp_KM"}, LadPrint, {"GPA5", "GPA_APMg", "GPA5_DSN.APMg", "GPA5_DSN.StartD", "GPA5_DSN.StartCrank", "GPA5_DSN.Rp_KM"})
Core.onExtChange({"GPA6_DSN.APMg", "GPA6_DSN.StartD", "GPA6_DSN.StartCrank", "GPA6_DSN.Rp_KM"}, LadPrint, {"GPA6", "GPA_APMg", "GPA6_DSN.APMg", "GPA6_DSN.StartD", "GPA6_DSN.StartCrank", "GPA6_DSN.Rp_KM"})
--КПК
Core.onExtChange({"GPA4_DSN.KPK", "GPA4_DSN.ZapK"}, LadPrint, {"GPA4", "GPA_KPK", "GPA4_DSN.KPK", "GPA4_DSN.ZapK"})
Core.onExtChange({"GPA5_DSN.KPK", "GPA5_DSN.ZapK"}, LadPrint, {"GPA5", "GPA_KPK", "GPA5_DSN.KPK", "GPA5_DSN.ZapK"})
Core.onExtChange({"GPA6_DSN.KPK", "GPA6_DSN.ZapK"}, LadPrint, {"GPA6", "GPA_KPK", "GPA6_DSN.KPK", "GPA6_DSN.ZapK"})
--ПЗМ
Core.onExtChange({"GPA4_DSN.PZM"}, LadPrint, {"GPA4", "GPA_PZM", "GPA4_DSN.PZM"})
Core.onExtChange({"GPA5_DSN.PZM"}, LadPrint, {"GPA5", "GPA_PZM", "GPA5_DSN.PZM"})
Core.onExtChange({"GPA6_DSN.PZM"}, LadPrint, {"GPA6", "GPA_PZM", "GPA6_DSN.PZM"})
--АОсс
Core.onExtChange({"GPA4_DSN.AOs", "GPA4_DSN.StrK"}, LadPrint, {"GPA4", "GPA_AOs", "GPA4_DSN.AOs", "GPA4_DSN.StrK"})
Core.onExtChange({"GPA5_DSN.AOs", "GPA5_DSN.StrK"}, LadPrint, {"GPA5", "GPA_AOs", "GPA5_DSN.AOs", "GPA5_DSN.StrK"})
Core.onExtChange({"GPA6_DSN.AOs", "GPA6_DSN.StrK"}, LadPrint, {"GPA6", "GPA_AOs", "GPA6_DSN.AOs", "GPA6_DSN.StrK"})
--АОбс
Core.onExtChange({"GPA4_DSN.AOb"}, LadPrint, {"GPA4", "GPA_AOb", "GPA4_DSN.AOb"})
Core.onExtChange({"GPA5_DSN.AOb"}, LadPrint, {"GPA5", "GPA_AOb", "GPA5_DSN.AOb"})
Core.onExtChange({"GPA6_DSN.AOb"}, LadPrint, {"GPA6", "GPA_AOb", "GPA6_DSN.AOb"})
--ВОсс
Core.onExtChange({"GPA4_DSN.VOs", "GPA4_DSN.StrK", "GPA4_DSN.Rp_MK"}, LadPrint, {"GPA4", "GPA_VOs", "GPA4_DSN.VOs", "GPA4_DSN.StrK", "GPA4_DSN.Rp_MK"})
Core.onExtChange({"GPA5_DSN.VOs", "GPA5_DSN.StrK", "GPA5_DSN.Rp_MK"}, LadPrint, {"GPA5", "GPA_VOs", "GPA5_DSN.VOs", "GPA5_DSN.StrK", "GPA5_DSN.Rp_MK"})
Core.onExtChange({"GPA6_DSN.VOs", "GPA6_DSN.StrK", "GPA6_DSN.Rp_MK"}, LadPrint, {"GPA6", "GPA_VOs", "GPA6_DSN.VOs", "GPA6_DSN.StrK", "GPA6_DSN.Rp_MK"})
--ВОбс
Core.onExtChange({"GPA4_DSN.VOb", "GPA4_DSN.Rp_MK"}, LadPrint, {"GPA4", "GPA_VOb", "GPA4_DSN.VOb", "GPA4_DSN.Rp_MK"})
Core.onExtChange({"GPA5_DSN.VOb", "GPA5_DSN.Rp_MK"}, LadPrint, {"GPA5", "GPA_VOb", "GPA5_DSN.VOb", "GPA5_DSN.Rp_MK"})
Core.onExtChange({"GPA6_DSN.VOb", "GPA6_DSN.Rp_MK"}, LadPrint, {"GPA6", "GPA_VOb", "GPA6_DSN.VOb", "GPA6_DSN.Rp_MK"})
--НОсс
Core.onExtChange({"GPA4_DSN.NOs", "GPA4_DSN.StrK", "GPA4_DSN.Rp_MK"}, LadPrint, {"GPA4", "GPA_NOs", "GPA4_DSN.NOs", "GPA4_DSN.StrK", "GPA4_DSN.Rp_MK"})
Core.onExtChange({"GPA5_DSN.NOs", "GPA5_DSN.StrK", "GPA5_DSN.Rp_MK"}, LadPrint, {"GPA5", "GPA_NOs", "GPA5_DSN.NOs", "GPA5_DSN.StrK", "GPA5_DSN.Rp_MK"})
Core.onExtChange({"GPA6_DSN.NOs", "GPA6_DSN.StrK", "GPA6_DSN.Rp_MK"}, LadPrint, {"GPA6", "GPA_NOs", "GPA6_DSN.NOs", "GPA6_DSN.StrK", "GPA6_DSN.Rp_MK"})
--НОбс
Core.onExtChange({"GPA4_DSN.NOb", "GPA4_DSN.Rp_MK"}, LadPrint, {"GPA4", "GPA_NOb", "GPA4_DSN.NOb", "GPA4_DSN.Rp_MK"})
Core.onExtChange({"GPA5_DSN.NOb", "GPA5_DSN.Rp_MK"}, LadPrint, {"GPA5", "GPA_NOb", "GPA5_DSN.NOb", "GPA5_DSN.Rp_MK"})
Core.onExtChange({"GPA6_DSN.NOb", "GPA6_DSN.Rp_MK"}, LadPrint, {"GPA6", "GPA_NOb", "GPA6_DSN.NOb", "GPA6_DSN.Rp_MK"})

Core.waitEvents();
