package helper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonUtils {

    public static Iterator<Object[]> readJsonFile(String filePath) throws FileNotFoundException {
        JsonElement jsonData = new JsonParser().parse(new FileReader(filePath));
        List<JsonElement> lstJson = new Gson().fromJson(jsonData, new TypeToken<List<JsonElement>>() {
        }.getType());
        List<Object[]> data = new ArrayList<>();
        for (JsonElement element : lstJson) {
            data.add(new Object[]{element});
        }
        return data.iterator();
    }
}
