/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Generators;

import java.util.ArrayList;

/**
 *
 * @author nazarov
 */
//  ======== класс объекта FB  ноды FBNetwork типов GraphicsCompositeFBType CompositeFBType=========
class FBV {

    ArrayList<FBVarValue> listVarValue = new ArrayList<>(); // список переменных полей самого FB

    // --- Добавляем элементы в список ---
    public void addVarValue(FBVarValue fbVar) {
        listVarValue.add(fbVar);
    }

    // --- Получить список элементов ---
    public ArrayList<FBVarValue> getListValue() {
        return listVarValue;
    }

    // --- сравнить кто есть и изменить совпадения ---
    public void editVar(ArrayList<String[]> match) {
        for (String[] s : match) {
            for (int i = 0; i < listVarValue.size(); i++) {
                FBVarValue value = listVarValue.get(i);
                if (value.getVariable().equals(s[0])) {
                    FBVarValue cutVal = listVarValue.get(i);
                    cutVal.editType(s[1]);
                    cutVal.editValue(s[2]);
                    cutVal.editText(s[3]);
                    checkTypeDataNode(value);
                    break;
                }
            }
        }
    }

    // --- сравнить кто есть в списке и удалить при совпадениях ---
    public void delVar(ArrayList<String> match) {
        for (String s : match) {
            for (int i = 0; i < listVarValue.size(); i++) {
                FBVarValue value = listVarValue.get(i);
                if (value.getVariable().equals(s)) {
                    listVarValue.remove(i);
                    break;
                }
            }
        }
    }

    // ---  Проверка типа данных ноды  String (обрамление кавычками '')---
    private void checkTypeDataNode(FBVarValue value) {
        String apos = "";
        if ("STRING".equals(value.getType())) {
            apos = "'";
            int fKov = value.getValue().indexOf("'");
            //System.out.println(fKov);
            if (fKov < 0) { // нет кавычки в начале то прикручиваем их
                value.editValue(apos + value.getValue() + apos);
            }
        }

    }

}