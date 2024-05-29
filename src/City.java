import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.CompletionStage;

public class City {
    String cityName;
    double xCoordinate;
    double yCoordinate;

    String before = "";
    public City(String cityName, double xCoordinate, double yCoordinate){
        this.cityName = cityName;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }
}
