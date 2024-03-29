Tag.GPA_crs = {
--Начало таблицы
'_AS.AO_Shop',  'АО сс',  -- АО из цеха;
'_AS.Fire_inOD',  'Пожар',  -- Пожар в отсеке ГТД;
'_AS.Fire_inUkr',  'Пожар',  -- Пожар в укрытии компрессора;
'_AS.Fire_inETO',  'Пожар',  -- Пожар в блоке электротехническом;
'_AS.Kr1sh_ON',  'АО бс',  -- Самопроизвольное закрытие Кр 1;
'_AS.Kr2sh_ON',  'АО бс',  -- Самопроизвольное закрытие Кр 2;
'_AS.Kr5sh_OF',  'АО бс',  -- Самопроизвольное открытие Кр 5;
'_AS.BEO_Work',  'АО сс',  -- Сработал ЭО;
'_AS.Vs_GPA_av',  'АО бс',  -- Опасная вибрация агрегата (любой из 7 каналов);
'_AS.Lm_MBD_dAn',  'АО бс',  -- Аварийный уровень масла в маслобаке ГТД;
'_AS.Gas20_ON',  'АО сс',  -- Загазованность 20 % НКПВ в отсеке компрессора;
'_AS.Gas20_OD',  'АО сс',  -- Загазованность 20 % НКПВ в отсека двигателя;
'_AS.Lm_MBK_dAn',  'АО сс',  -- Аварийно-низкий уровень масла в маслобаке Б1 компрессора;
'_AS.Lm_Nkar_dAn',  'АО сс',  -- Аварийно-низкий уровень масла в картере компрессора;
'_AS.No_Qm_Lub1',  'АО сс',  -- Нет потока масла в лубрикантной системе компрессора ц. 1;
'_AS.No_Qm_Lub2',  'АО сс',  -- Нет потока масла в лубрикантной системе компрессора ц. 2;
'_AS.L_freeze_Bak_dAn',  'АО сс',  -- Аварийный уровень охлаждающей жидкости в баке компрессора;
'_AS.Vs_N_av',  'АО бс',  -- Опасная вибрация компрессора;
'_AS.SK_OF',  'АО бс',  -- Самопроизвольное закрытие ОГК;
'_AS.N_TK_BZRT_av',  'АО сс',  -- Предельные обороты ТВД (БЗРТ);
'_AS.N_ST_BZRT_av',  'АО сс',  -- Предельные обороты СТ (БЗРТ);
'_AS.T_outGG_av',  'АО сс',  -- Предельная температура за ТК (БЗРТ);
'_AS.AOs_ARM',  'АО сс',  -- АОсс по команде оператора;
'_AS.AOb_ARM',  'АО бс',  -- АОбс по команде оператора;
'_AS.AOs_PRU',  'АО сс',  -- АОсс от РПКУ;
'_AS.AOb_PRU',  'АО бс',  -- АОбс от РПКУ;
'_AS.Kn_AO_ETO',  'АО сс',  -- АО по кнопке на блоке ЭТ и САУ;
'_AS.Kn_EO_PRU',  'АО сс',  -- АО по кнопке на ПУ;
'_AS.StartD_lock',  'АО бс',  -- Зависание на запуске;
'_AS.AOb_FR',  'АО бс',  -- Обобщенный АОбс от регулятора;
'_AS.Nvd_bad',  'АО бс',  -- Отказ каналов измерения оборотов ТК;
'_AS.T_outGG_bad',  'АО бс',  -- Отказ каналов измерения температуры газа за турбиной;
'_AS.NstJamp',  'АО бс',  -- Раскрутка СТ;
'_AS.noFlame',  'АО бс',  -- Нет розжига;
'_AS.Fakel_BreakDown',  'АО бс',  -- Погасание факела;
'_AS.Surge',  'АО бс',  -- Помпаж двигателя;
'_AS.Nst_bad',  'АО сс',  -- Отказ каналов измерения оборотов СТ;
'_AS.Msm_K_avar',  'АО бс',  -- Авария маслосистемы;
'_AS.AOss_TU',  'АО сс',  -- Переход в АОсс по техническим условиям;
'_AS.AObs_TU',  'АО бс',  -- Переход в АОбс по техническим условиям;
'_AS.Ptg_inTRK_v',  'АО бс',  -- Утечка топливного газа через ОГК;
'_AS.Tv_OD_Av',  'АО бс',  -- Высокая температура в отсеке Д;
'_AS.Tg_outNag1_Av',  'АО сс',  -- Высокая Т газа на выходе 1-й ступени компрессора;
'_AS.Tg_outNag_Av',  'АО сс',  -- Высокая температура газа на выходе компрессора;
'_AS.Tg_outN_C1_Av',  'АО сс',  -- Высокая температура газа на выходе цилиндра 1;
'_AS.Tg_outN_C2_Av',  'АО сс',  -- Высокая температура газа на выходе цилиндра 2;
'_AS.Tg_outN_C3_Av',  'АО сс',  -- Высокая температура газа на выходе цилиндра 3;
'_AS.Tg_outN_C4_Av',  'АО сс',  -- Высокая температура газа на выходе цилиндра 4;
'_AS.Tg_outN_C5_Av',  'АО сс',  -- Высокая температура газа на выходе цилиндра 5;
'_AS.Tg_outN_C6_Av',  'АО сс',  -- Высокая температура газа на выходе цилиндра 6;
'_AS.Tmsm_inN_Av',  'АО сс',  -- Высокая температура масла на входе в компрессор;
'_AS.T_freeze_outDC_Av',  'АО сс',  -- Высокая температуры охл. жидкости на выходе драйкуллера;
'_AS.T_KP1_N_Av',  'АО сс',  -- Высокая Т коренного подшипника 1 компрессора;
'_AS.T_KP2_N_Av',  'АО сс',  -- Высокая Т коренного подшипника 2 компрессора;
'_AS.T_KP3_N_Av',  'АО сс',  -- Высокая Т коренного подшипника 3 компрессора;
'_AS.T_KP4_N_Av',  'АО сс',  -- Высокая Т коренного подшипника 4 компрессора;
'_AS.T_KP5_N_Av',  'АО сс',  -- Высокая Т коренного подшипника 5 компрессора;
'_AS.T_KP6_N_Av',  'АО сс',  -- Высокая Т коренного подшипника 6 компрессора;
'_AS.T1_UP_N_Av',  'АО сс',  -- Высокая Т упорного подшипника компрессора (т. 1);
'_AS.T2_UP_N_Av',  'АО сс',  -- Высокая Т упорного подшипника компрессора (т. 2);
'_AS.Tm_inR_Av',  'АО бс',  -- Высокая температура масла на входе в редуктор;
'_AS.Pg_inN_An',  'АО сс',  -- Низкое давление газа на входе в компрессор;
'_AS.Pg_inN_Av',  'АО сс',  -- Высокое давление газа на входе компрессора;
'_AS.Pg_outN_Av',  'АО сс',  -- Высокое давление газа на выходе компрессора;
'_AS.Vs_ZONg_Av',  'АО сс',  -- Предельная вибрация компрессора со стороны привода( ГС );
'_AS.Vs_PONg_Av',  'АО сс',  -- Предельная вибрация компрессора со стороны маслонасоса( ГС );
'_AS.Vs_PO_TK_Av',  'АО бс',  -- Высокая вибрация по переднему стоечному узлу ТК;
'_AS.Vs_ZO_TK_Av',  'АО бс',  -- Высокая вибрация по задней опоре ротора ТК;
'_AS.Vs_ST_Av',  'АО сс',  -- Высокая вибрация по опоре ротора СТ;
'_AS.Pg_outGPA_Av',  'АО сс',  -- Высокое давление газа на выходе ГПА;
'_AS.Pmsm_inN_An',  'АО сс',  -- Низкое давление масла на входе в компрессор;
'_AS.Pmsm_inN_Av',  'АО сс',  -- Высокое давление масла на входе в компрессор;
'_AS.Pv_uprRP_N_An',  'АО сс',  -- Низкое Р воздуха упр. РП и УОП цилиндров компрессора;
'_AS.Pv_uprRP_N_Av',  'АО сс',  -- Высокое Р воздуха упр. РП и УОП цилиндров компрессора;
'_AS.P_utech_RP_Av',  'АО сс',  -- Высокое давление утечек из рабочей полости;
'_AS.Pm_lub_An',  'АО бс',  -- Низкое давление в лубрикаторной системе;
'_AS.Pm_lub_Av',  'АО бс',  -- Высокое давление в лубрикаторной системе;
'_AS.L_freeze_Bak_An',  'АО сс',  -- Низкий уровень охлаждающей жидкости в баке;
'_AS.L_freeze_Bak_Av',  'АО сс',  -- Высокий уровень охлаждающей жидкости в баке;
'_AS.Q_freeze_An',  'АО сс',  -- Низкий расход охлаждающей жидкости;
'_AS.Pm_inR_An',  'АО бс',  -- Низкое давление масла на входе в редуктор на запуске;
'_AS.Pm_inR_An1',  'АО бс',  -- Низкое давление масла на входе в редуктор;
'_AS.Pv_outTK_Av',  'АО бс',  -- Высокое статическое давление воздуха за компрессором;
'_AS.Pm_inD_An',  'АО бс',  -- Низкое давление масла входе в двигатель на запуске;
'_AS.Lm_MBD_Av',  'ВО бс',  -- Высокий уровень масла в МБД;
'_AS.Ptg_GPA_An',  'АО бс',  -- Низкое давление топливного газа (на запуске);
'_AS.Tg_outAVOG1_Av',  'АО сс',  -- Высокая температура газа на выходе АВОГ 1-й ступени компрессора;
'_AS.ev_Nst_Av',  'АО сс',  -- Высокие обороты СТ;
'_AS.ev_Tg_outAVOG2_Av',  'АО сс',  -- Высокая температура газа на выходе АВОГ 2-й ступени компрессора;
'_AS.ev_wrong_TRK_Av',  'АО бс',  -- Рассогласование по ДГ;
'_AS.ev_wrong_VNA_Av',  'АО бс',  -- Рассогласование по ВНА;
'_AS.ES_bad',  'АО бс',  -- Питание РЧТиН ЭС неисправно (НКУ);
'_AS.PNSR_nRdy',  'АО бс',  -- Насос предпусковой прокачки масла редуктора неисправен (НКУ);
'_AS.Lm_MBL_dAn',  'ВО сс',  -- Аварийный уровень масла в МБЛ (низкий);
'_AS.Lm_MBR_dAv',  'ВО бс',  -- Аварийно-высокий уровень масла в маслобаке редуктора;
'_AS.Lm_MBR_dAn',  'ВО бс',  -- Аварийно-низкий уровень масла в маслобаке редуктора;
'_AS.TEN_MBR_bad',  'ВО бс',  -- Электронагреватели масла в маслобаке редуктора неисправны;
'_AS.GN_Rdk_bad',  'ВО бс',  -- Неисправность главного насоса редуктора;
'_AS.GKS_Rdk_bad',  'ВО бс',  -- Неисправность главного контура смазки редуктора;
'_AS.T_freeze_Bak_Ov',  'ВО сс',  -- Высокая температура охлаждающей жидкости в баке;
'_AS.T_POP_PromVal_Ov',  'АО бс',  -- Высокая Т опорного подш. промежуточного вала (ст. компрессора);
'_AS.T_POP_SlowVal_Ov',  'АО бс',  -- Высокая Т опорного подшипника тихоходного вала (ст. компрессора);
'_AS.T_ZOP_PromVal_Ov',  'АО бс',  -- Высокая Т опорного подш. промежуточного вала (ст. двигателя);
'_AS.T_ZOP_SlowVal_Ov',  'АО бс',  -- Высокая Т опорного подшипника тихоходного вала (ст. двигателя);
'_AS.T_PUP_SlowVal_Ov',  'АО бс',  -- Высокая Т упорного подшипника тихоходного вала (ст. компрессора);
'_AS.T_ZOP_FastVal_Ov',  'АО бс',  -- Высокая Т опорного подш. быстроходного вала (ст. двигателя);
'_AS.T_POP_FastVal_Ov',  'АО бс',  -- Высокая Т опорного подш. быстроходного вала (ст. компрессора);
'_AS.T_ZUP_SlowVal_Ov',  'АО бс',  -- Высокая Т упорного подшипника тихоходного вала (ст. двигателя);
'_AS.Tm_outZO_TK_Ov',  'ВО бс',  -- Высокая температура масла слива задней опоры ТК;
'_AS.Tm_outST_Ov',  'ВО бс',  -- Высокая температура масла на сливе из опор СТ;
'_AS.Tm_outPO_TK_Ov',  'ВО бс',  -- Высокая температура масла слива передней опоры ТК;
'_AS.Tm_outR_Ov',  'АО бс',  -- Высокая температура масла на выходе из редуктора;
'_AS.Tm_inD_Ov',  'ВО бс',  -- Высокая температура масла на входе в двигатель;
'_AS.Tm_MBD_Ov',  'ВО бс',  -- Высокая температура масла в МБД;
'_AS.Vp_rd_SlowVal_g_Ov',  'АО бс',  -- Опасное радиальное виброперемещение тихоходного вала (гор.);
'_AS.Vp_rd_SlowVal_v_Ov',  'АО бс',  -- Опасное радиальное виброперемещение тихоходного вала (верт.);
'_AS.Vp_ax_SlowVal_Ov',  'АО бс',  -- Опасное аксиальное виброперемещение тихоходного вала вперед, т.1;
'_AS.Vp_ax_SlowVal_On',  'АО бс',  -- Опасное аксиальное виброперемещение тихоходного вала назад, т.1;
'_AS.Vp_ax_SlowVal_Res_Ov',  'АО бс',  -- Опасное аксиальное виброперемещение тихоходного вала вперед, т.2;
'_AS.Vp_ax_SlowVal_Res_On',  'АО бс',  -- Опасное аксиальное виброперемещение тихоходного вала назад, т.2;
'_AS.Vp_rd_FastVal_g_Ov',  'АО бс',  -- Опасное радиальное виброперемещение быстроходного вала (гор.);
'_AS.Vp_rd_FastVal_v_Ov',  'АО бс',  -- Опасное радиальное виброперемещение быстроходного вала (верт.);
'_AS.Lm_MBL_On',  'ВО бс',  -- Низкий уровень масла в маслобаке лубрикаторной системы;
'_AS.Lm_MBR_On',  'АО бс',  -- Низкий уровень масла в маслобаке редуктора;
'_AS.Lm_MBR_Ov',  'АО бс',  -- Высокий уровень масла в маслобаке редуктора;
'_AS.Pm_inD_On',  'ВО бс',  -- Низкое давление масла на входе в двигатель на режиме;
'_AS.Rm_struj_PO_TK_Ov',  'ВО бс',  -- Стружка в масле на сливе из передней опоры ТК;
'_AS.Rm_struj_ZO_TK_Ov',  'ВО бс',  -- Стружка в масле на сливе из задней опоры ТК;
'_AS.Rm_struj_ST_Ov',  'ВО бс',  -- Стружка в масле на сливе из опоры СТ;
'_AS.CNR_nRdy',  'ВО бс',  -- Насос циркуляции масла редуктора неисправен;
'_AS.FS1_Sliv1_bad',  'ВО сс',  -- Неисправность системы слива 1-ой ступени ФС 1 ;
'_AS.FS1_Sliv2_bad',  'ВО сс',  -- Неисправность системы слива 2-ой ступени ФС 1;
'_AS.KE5_bad',  'АО сс',  -- Неисправность клапана КШЭ 5;
'_AS.FS2_Sliv_bad',  'ВО сс',  -- Неисправность системы слива 1-ой ступени ФС 2;
'_AS.FS3_Sliv1_bad',  'ВО сс',  -- Неисправность системы слива 1-ой ступени ФС 3;
'_AS.FS3_Sliv2_bad',  'ВО сс',  -- Неисправность системы слива 2-ой ступени ФС 3;
'_AS.hiT_outTK',  'АО бс',  -- Высокая Т за турбиной;
'_AS.Fall_PmsmK_Pusk',  'ВО бс',  -- Неисправность основного насоса компрессора на пуске;
'_AS.MSK_Brk',  'ВО бс',  -- Неисправность системы смазки компрессора;
'_AS.Fall_PmsmK2',  'ВО бс',  -- Повторная просадка Рм компрессора;
'_AS.Fall_PmsmR_Pusk',  'ВО бс',  -- Неисправность главного насоса редуктора (на запуске);
'_AS.Fall_PmsmR2',  'ВО бс',  -- Повторная просадка Рм редуктора;
'_AS.A_NvdNormHigh',  'АО бс',  -- Высокие обороты ТВД приведённые;
'_AS.A_noFlame',  'АО бс',  -- Нет горения;
'_AS.Lm_MBL_dAv',  'ВО бс',  -- Аварийный уровень масла в МБЛ (высокий);
'_AS.Pm_inK_outF_An',  'АО сс',  -- Низкое давление масла на вх. в компрессор после фильтров;
'_AS.Pm_inK_outF_Av',  'АО сс',  -- Высокое давление масла на вх. в компрессор после фильтров;
'_AS.StokForce1S_av',  'АО сс',  -- Высокая нагрузка на шток 1 ст. компрессора при сжатии;
'_AS.StokForce1R_av',  'АО сс',  -- Высокая нагрузка на шток 1 ст. компрессора при растяжении;
'_AS.StokForce2S_av',  'АО сс',  -- Высокая нагрузка на шток 2 ст. компрессора при сжатии;
'_AS.StokForce2R_av',  'АО сс',  -- Высокая нагрузка на шток 2 ст. компрессора при растяжении;
'_AS.dT_KP_av',  'АО сс',  -- Высокая разность температур кореных подшипников;
'_AS.Vp_SlowVal_d',  'АО бс',  -- Опасное виброперемещение тихоходного вала редуктора (дискр.);
'_AS.Vp_FastVal_d',  'АО бс',  -- Опасное виброперемещение быстроходного вала редуктора (дискр.);
'_AS.A_NstHigh',  'АО бс',  -- Низкие обороты СТ на режиме;
'_AS.A_NvdLow',  'АО бс',  -- Низкие приведенные обороты ТК на режиме;
'_AS.CNVO_brk',  'АО бс',  -- НОХ неисправен;
'_AS.PNSN_brk',  'АО бс',  -- НППМК неисправен;
'_AS.Pv_uprRP_N_brk',  'АО сс',  -- Отказ датчика давления воздуха РП и УОП;
'_AS.Pmsm_inN_brk',  'АО сс',  -- Отказ датчика давления маслосмазки на входе в компрессор;
'_AS.Pg_outN1_brk',  'АО сс',  -- Отказ датчика давления газа на выходе 1 ступени компрессора;
'_AS.Pg_outN_brk',  'АО сс',  -- Отказ датчика давления газа на выходе компрессора;
'_AS.Tg_outAVOG1_brk',  'АО сс',  -- Отказ датчика температуры газа на выходе АВОГ 1 ст. компрессора;
'_AS.Tg_outAVOG2_brk',  'АО сс',  -- Отказ датчиков температуры газа на выходе АВОГ 2 ст. компрессора;
'_AS.T_utech_salnik_Av',  'АО сс',  -- Прорыв сальников штока;
'_AS.Ptg_inTRK',  'ВО бс',  -- Низкое давление ТГ перед ТРК;
'_AS.Pm_lub_brk',  'ВО бс',  -- Отказ датчика давления масла в МБ лубрикаторной системы компрессора;
'_AS.T_KP_N_brk',  'ВО бс',  -- Отказ датчика температуры коренных подшипников;
'_AS.Tmsm_inN_brk',  'ВО сс',  -- Отказ датчика температуры масла на входе в компрессора;
'_AS.P_utech_RP_brk',  'ВО сс',  -- Отказ датчика давления утечеки из РП и УОП;
'_AS.Pg_inN_brk',  'ВО сс',  -- Отказ датчика давления газа на входе компрессора;
'_AS.Pg_outAVOG2_brk',  'ВО сс',  -- Отказ датчиков давления газа на выходе АВОГ-2;
'_AS.Tg_inN_brk',  'ВО сс',  -- Отказ датчика температуры газа на входе компрессора;
'_AS.T_freeze_outDC_brk',  'ВО сс',  -- Отказ датчика температуры охлаждающей жидкости на выходе драйкуллера;
'_AS.T_utech_salnik_brk',  'ВО сс',  -- Отказ датчика температуры первичных утечек от сальников штока;
'_AS.U_Q1Q2_Start_bad',  'АО бс',  -- Нет напряжения вводов 1 и 2 НКУ при пуске;
'_AS.U_Q1Q2_Work_bad',  'АО бс',  -- Нет напряжения вводов 1 и 2 НКУ на режиме;
'_AS.U_Q4_bad',  'АО бс',  -- Нет напряжения шин =220 В;
'_AS.U_DC24_48_bad',  'АО бс',  -- Нет напряжения шин =24/ =48 B;
'_AS.Lm_MBD_An',  'ВО бс',  -- Низкий уровень масла в МБД;
'_AS.A_NvdHigh',  'АО бс',  -- Высокие обороты ТВД;
'_AS.L_freeze_dAv',  'АО сс'  -- Высокий уровень охлаждающей жидкости в баке (дискр.);
  -- Конец таблицы
}
