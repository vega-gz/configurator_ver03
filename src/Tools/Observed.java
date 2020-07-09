/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Tools;

/**
 *
 * @author Nazarov
 */

// --- Класс наблюдатель состояния объектов для таск бара ---


// --- интерфейс за кем наблюдаем ---
public interface Observed{
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObserver();
}

