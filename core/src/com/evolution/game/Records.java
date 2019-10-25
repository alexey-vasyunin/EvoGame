package com.evolution.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.evolution.game.screens.ScreenManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Records implements Serializable {
    private ArrayList<Record> records;
    private transient FileHandle fh;
    private transient StringBuilder sb;

    public ArrayList<Record> getRecords() {
        return records;
    }

    private static Records ourInstance = new Records();

    public static Records getInstance() {
        return ourInstance;
    }

    private Records(){
        fh = Gdx.files.local("records.dat");
        records = new ArrayList<Record>();
        for (int i = 0; i < 5; i++) {
            records.add(new Record("Player", 0));
        }
        sb = new StringBuilder();
    }

    public void load(){
        System.out.println(fh.exists());
        if (fh.exists()){
            try {
                ObjectInputStream ois = new ObjectInputStream(fh.read());
                records = (ArrayList<Record>)ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void save(){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(fh.write(false));
            oos.writeObject(records);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(String name, int score){
        records.add(new Record(name, score));
        Collections.sort(records, new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                return o2.score - o1.score;
            }
        });
    }

    public StringBuilder getRecordString(int index){
        sb.setLength(0);
        return sb.append(records.get(index).name).append(": ").append(records.get(index).score);
    }

    public static class Record implements Serializable{
        public String name;
        public int score;

        public Record(String name, int score) {
            this.name = name;
            this.score = score;
        }

    }
}
