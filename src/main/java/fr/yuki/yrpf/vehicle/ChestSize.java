package fr.yuki.yrpf.vehicle;

public class ChestSize {
    public static int getChestSizeByModelId(int modelId) {
        switch (modelId) {
            case 6:
            case 32:
            case 10:
            case 20:
            case 26:
                return 5;

            case 25:
                return 15;

            case 11:
            case 12:
            case 33:
            case 34:
                return 20;

            case 1:
            case 2:
            case 3:
            case 19:
            case 30:
            case 31:
                return 30;

            case 4:
            case 5:
            case 29:
                return 25;

            case 21:
            case 22:
            case 23:
            case 27:
                return 40;

            case 35:
            case 36:
                return 45;

            case 7:
            case 8:
            case 9:
            case 13:
            case 14:
            case 15:
            case 16:
                return 50;

            case 17:
            case 18:
            case 24:
                return 80;

            case 28:
                return 100;

            default: return 50;
        }
    }
}
