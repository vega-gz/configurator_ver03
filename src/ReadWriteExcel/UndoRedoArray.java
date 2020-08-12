/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReadWriteExcel;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cherepanov
 */
public class UndoRedoArray {
    public List<Integer> currentValues;
    public int version = 0;
    public int highestVersion = 0;
    public Map<Integer, List<Integer>> history = new HashMap<Integer,List<Integer>>();

    public Integer[] getValues() {
        return (Integer[]) getCurrentValues().toArray();//конвертируем наш список в массив
    }

    public List<Integer> getCurrentValues() {//метод проверки,если список пустой (то есть его нет)то инициализируем
        if (currentValues == null) {
            currentValues = new ArrayList<Integer>();
        }
        return currentValues;
    }

     public void add(Integer[] newValues) {//метод добавления значений в список текущих значений
        incrementHistory();//обновляем версию
        getCurrentValues().addAll(Arrays.asList(newValues));
    }

    public void incrementHistory() {//метод обновления версий
        if (history.get(version) != null)  {//проверяем ,не пустая ли история
            throw new IllegalArgumentException("Cannot change history");
        }
        history.put(version,getCurrentValues());//помещаем индекс
        if (version > 2) {
            history.remove(version - 2);//удаляем элемент из списка
        }
        version++;
        if (version > highestVersion) {//если текущая верся(индекс)больше главное,то текущая становится главной(то бишь последней версией)
            highestVersion = version;//
        }
    }

    public void delete(Integer[] endValues) {//метод очистки массива текущих значений
        incrementHistory();//обновляем версию
        int currentLength = getCurrentValues().size();//количество совершенных изменений
        int i = endValues.length-1;//-1 скорее всего потому что отсчет начинается от 0
        for (int deleteIndex = currentLength - 1//опять таки потому что от 0
                ; deleteIndex > currentLength - endValues.length
                ; deleteIndex--) {
            if (!endValues[i].equals(getCurrentValues().get(deleteIndex))//проверка на наличие,если не равны,значит этого символа нет
                    ) {
                throw new IllegalArgumentException("Cannot delete element(" + endValues[i] + ") that isn't there");                
            }
            getCurrentValues().remove(deleteIndex);
        }
    }

    public void undo() {//откат изменений вниз по индексу
       version--;//откатываемся и получаем значение
       if (history.get(version) == null) {
           throw new RuntimeException("Undo operation only supports 2 undos");
       }
       this.currentValues = history.get(version);
    }

    public void redo() {//вверх по индексу
       version++;
       if (history.get(version) == null) {
           throw new RuntimeException("Redo operation only supported after undo");
       }
       this.currentValues = history.get(version);
    }
    
   
}
