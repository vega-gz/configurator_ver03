IP_HMI1 = "127.0.0.1:10001"
IP_HMI2 = "127.0.0.1:10002"

------------------------------- Переключение Мнемо ------------------------------- Steam
local function ChangeWnd(Wnd_OnOf)
	Core.directSet(Wnd_OnOf[1], 1, 4294967289, 0, true, "BOOL"); --Wnd_On
	Core.directSet(Wnd_OnOf[2], 1, 4294967289, 0, false, "BOOL"); --Wnd_Of
end

Core.onExtChange({"Show_HMI1"}, ChangeWnd, {IP_HMI1, IP_HMI2});
Core.onExtChange({"Show_HMI2"}, ChangeWnd, {IP_HMI2, IP_HMI1});

Core.waitEvents();