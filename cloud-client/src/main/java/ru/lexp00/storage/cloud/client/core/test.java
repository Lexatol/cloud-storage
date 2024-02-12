package ru.lexp00.storage.cloud.client.core;

public class test {
    public static void main(String[] args) {
        int dateFile = 60001;
        int sizePacket = 1000;
        int count = getCount(dateFile, sizePacket);
        System.out.println(count);
    }

    private static int getCount(int dateFile, int sizePacket) {
        int count = 0;
        for (int i = 0; i < dateFile/sizePacket; i++) {
            count++;
        }
        int countNext = dateFile - count * sizePacket;
        if (countNext < sizePacket && countNext != 0) {
            count++;
        }
        return count;
    }
}
