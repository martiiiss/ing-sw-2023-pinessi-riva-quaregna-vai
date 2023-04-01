package model;


public class CGC7 implements CGMStrategy {
    public boolean compareRule(Bookshelf bks, int id){
        int counter = 0;
        int numOfBlocks = 0;

        Tile[][] mini = new Tile[2][2];
        Tile[][] bookshelf = bks.getBookshelf();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                mini[0][0] = bookshelf[i][j];
                mini[0][1] = bookshelf[i][j + 1];
                mini[1][0] = bookshelf[i + 1][j];
                mini[1][1] = bookshelf[i + 1][j + 1];
                Type temp = bookshelf[i][j].getType();
                for (int x = 0; x < 2; x++) {
                    for (int y = 0; y < 2; y++) {
                        if (temp.equals(mini[x][y].getType()) && temp.equals(Type.NOTHING))
                            counter++;
                    }
                }
                if (counter == 4 && j < 2 && i < 2)//TopLeft
                    if (!temp.equals(bookshelf[i][j + 2].getType()) && !temp.equals(bookshelf[i + 1][j + 2].getType()) && !temp.equals(bookshelf[i + 2][j].getType()) && !temp.equals(bookshelf[i + 2][j + 1].getType())) {
                        numOfBlocks++;
                        counter = 0;
                        break;
                    }
                if (counter == 4 && j < 2 && i >= 2) //Left side
                    if (!temp.equals(bookshelf[i][j + 2].getType()) && !temp.equals(bookshelf[i + 1][j + 2].getType()) && !temp.equals(bookshelf[i + 2][j].getType()) && !temp.equals(bookshelf[i + 2][j + 1].getType()) && !temp.equals(bookshelf[i - 1][j].getType()) && temp.equals(bookshelf[i - 1][j + 1].getType())) {
                        numOfBlocks++;
                        counter = 0;
                        break;
                    }
                if (counter == 4 && j < 2 && i + 2 >= 6) //BotLeft
                    if (!temp.equals(bookshelf[i][j + 2].getType()) && !temp.equals(bookshelf[i + 1][j + 2].getType()) && !temp.equals(bookshelf[i - 1][j].getType()) && !temp.equals(bookshelf[i - 1][j + 1].getType())) {
                        numOfBlocks++;
                        counter = 0;
                        break;
                    }
                if (counter == 4 && j + 2 < 5 && i + 2 >= 6) //Bot side
                    if (!temp.equals(bookshelf[i][j + 2].getType()) && !temp.equals(bookshelf[i + 1][j + 2].getType()) && !temp.equals(bookshelf[i - 1][j].getType()) && !temp.equals(bookshelf[i - 1][j + 1].getType()) && !temp.equals(bookshelf[i][j - 1].getType()) && !temp.equals(bookshelf[i + 1][j - 1].getType())) {
                        numOfBlocks++;
                        counter = 0;
                        break;
                    }
                if (counter == 4 && j + 2 >= 5 && i + 2 >= 6) //BotRight
                    if (!temp.equals(bookshelf[i - 1][j].getType()) && temp.equals(bookshelf[i - 1][j + 1].getType()) && !temp.equals(bookshelf[i][j - 1].getType()) && !temp.equals(bookshelf[i + 1][j - 1].getType())) {
                        numOfBlocks++;
                        counter = 0;
                        break;
                    }
                if (counter == 4 && j + 2 >= 5 && i >= 2) //R side
                    if (!temp.equals(bookshelf[i - 1][j].getType()) && !temp.equals(bookshelf[i - 1][j + 1].getType()) && !temp.equals(bookshelf[i][j - 1].getType()) && !temp.equals(bookshelf[i + 1][j - 1].getType()) && !temp.equals(bookshelf[i + 2][j].getType()) && !temp.equals(bookshelf[i + 2][j + 1].getType())) {
                        numOfBlocks++;
                        counter = 0;
                        break;
                    }
                if (counter == 4 && j + 2 >= 5 && i < 2) //TopRight
                    if (!temp.equals(bookshelf[i][j - 1].getType()) && !temp.equals(bookshelf[i + 1][j - 1].getType()) && !temp.equals(bookshelf[i + 2][j].getType()) && !temp.equals(bookshelf[i + 2][j + 1].getType())) {
                        numOfBlocks++;
                        counter = 0;
                        break;
                    }
                if (counter == 4 && j >= 2 && i < 2) //Upper side
                    if (!temp.equals(bookshelf[i][j - 1].getType()) && !temp.equals(bookshelf[i + 1][j - 1].getType()) && !temp.equals(bookshelf[i + 2][j].getType()) && !temp.equals(bookshelf[i + 2][j + 1].getType()) && !temp.equals(bookshelf[i][j + 2].getType()) && !temp.equals(bookshelf[i + 1][j + 2].getType())) {
                        numOfBlocks++;
                        counter = 0;
                        break;
                    }
                /**Effettivamente questa condizione è sempre falsa, nella versione semplificata che ho fatto usando una matrice di int però è necessaria...*/
                if (counter == 4 && j>2 && i>2 && j+2<5 && i+2<6) //Not along the borders
                    if (!temp.equals(bookshelf[i][j - 1].getType()) && !temp.equals(bookshelf[i + 1][j - 1].getType()) && !temp.equals(bookshelf[i + 2][j].getType()) && !temp.equals(bookshelf[i + 2][j + 1].getType()) && !temp.equals(bookshelf[i][j + 2].getType()) && !temp.equals(bookshelf[i + 1][j + 2].getType()) && !temp.equals(bookshelf[i - 1][j].getType()) && !temp.equals(bookshelf[i - 1][j + 1].getType())) {
                        numOfBlocks++;
                        counter = 0;
                        break;
                    }
                counter = 0;
            }
        }
        return numOfBlocks == 2;
    }
}
