/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author cherepanov
 */
public class Struct {
     private String Name;
    private String UUID;
    private String Type;
    private ArrayList<Map> listData = new ArrayList(); // Может нужен HashMap

    public Struct(String Name, String UUID, String Type) {
        this.Name = Name;
        this.UUID = UUID;
        this.Type = Type;
    }

    public void addData(String Name, String Type, String UUID, String Comment) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("Name", Name);
        hashMap.put("Type", Type);
        hashMap.put("UUID", UUID);
        hashMap.put("Comment", Comment);
        hashMap.put("TypeUUID", this.Type); // должен занестись при создании
        listData.add(hashMap);
    }

    public String getUUD() {
        return UUID;
    }

    public String getName() {
        return Name;
    }

   public String getType() {
        return Type;
    }

    public ArrayList<Map> getlistData() {
        return listData;
    }

    void getAllData() {
        Iterator<Map> iter_arg = listData.iterator();
        while (iter_arg.hasNext()) {  //перебираем наш лист с Мапом
            Map<String, String> hashMap = iter_arg.next(); // Новый мап с нашими данными

            /*for (String value : hashMap.values()) { // перебираем все значения в самом Мапе но нам нужно с ключами
             System.out.println( "Value: " + value);
             }*/
            for (Map.Entry<String, String> item : hashMap.entrySet()) {
                System.out.printf("Key: %s  Value: %s \n", item.getKey(), item.getValue());
            }

        }
    }
}
