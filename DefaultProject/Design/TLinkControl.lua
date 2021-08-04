
--------------------------- Контроль связи с узлом ПЛК --------------- Steam
local function LinkState(ip_plc, name_plc, name_srv)
--  local  run_PLC = false
--  local  StatePLC =''
  local _,StPLC,_,_,_ = Core.directGet(ip_plc .. ':10000', 0.1, '@STATE', 0)
  local _,RunPLC,_,_,_ = Core.directGet(ip_plc .. ':10000', 0.1, '@NODE_RUN', 0)
  --  if StPLC ~= nil then StatePLC = StPLC end
  -- if StRun_PLC ~= nil then run_PLC = StRun_PLC end
  --if StatePLC == '' or not run_PLC then 
  --		Core['NoLink.GPA1']= true   
  if StPLC == nil or RunPLC == nil or StPLC == "" or not RunPLC then
		fl_noLink = true 
    	Core.addEvent("Нет связи с контроллером" .. name_plc , 10100, 1, name_plc .. "_Диаг.", name_srv, "NoLink " .. 
													name_plc .. "_" .. name_srv .. "_Нет связи с контроллером", DT, "")
  	else 
		fl_nolink = false 
		Core.addEvent("Нет связи с контроллером" .. name_plc , 10100, 0, name_plc .. "_Диаг.", name_srv, "NoLink " .. 
													name_plc .. "_" .. name_srv .. "_Нет связи с контроллером", DT, "")
  end
  return fl_nolink	
end

--------------------------- Контроль связи с узлом Сервера --------------- Steam
local function LinkStateSrv(ip_srv)
  local _,StSrv,_,_,_ = Core.directGet(ip_srv .. ':10000', 0.1, '@STATE', 0)
  local _,RunSrv,_,_,_ = Core.directGet(ip_srv .. ':10000', 0.1, '@NODE_RUN', 0)
  fl_noLink = StSrv == nil or RunPSrv == nil or StSrv == "" or not RunSrv
  return fl_nolink
end	
---------------------------------------------------------------------------------------------------------
function get_diag()
 Core.NoLink.KC   = LinkState("172.16.1.11", "КЦ",  Core.ThisSrv) -- Связь с ПЛК КЦ
 Core.NoLink.UPA  = LinkState("172.16.1.21", "УПА", Core.ThisSrv) -- Связь с ПЛК УПА
 Core.NoLink.UVI  = LinkState("172.16.1.31", "УВИ", Core.ThisSrv) -- Связь с ПЛК УВИ
 Core.NoLink.TSK  = LinkState("172.16.1.41", "ТСК", Core.ThisSrv) -- Связь с ПЛК ТСК
 Core.NoLink.NMS  = LinkState("172.16.1.51", "НМС", Core.ThisSrv) -- Связь с ПЛК НМС
 Core.NoLink.GPA4 = LinkState("172.16.1.24", "ГПА4", Core.ThisSrv) -- Связь с ПЛК ГПА4
 Core.NoLink.GPA5 = LinkState("172.16.1.25", "ГПА5", Core.ThisSrv) -- Связь с ПЛК ГПА5
 Core.NoLink.GPA6 = LinkState("172.16.1.26", "ГПА6", Core.ThisSrv) -- Связь с ПЛК ГПА4
 Core.NoLink.Server2 = LinkStateSrv("172.16.1.112") -- Связь с другим сервером 
end  

Core.onTimer(3, 1,get_diag, true, true)
Core.waitEvents();