package Models;

import com.google.gson.*;
import java.lang.reflect.Type;

public class JsonToVehicleMapper implements JsonDeserializer {
    @Override
    public Object deserialize(JsonElement jsonElement, Type type,
                              JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if(jsonObject.has("wheelSize")){
            return jsonDeserializationContext.deserialize(jsonObject, Bike.class);
        } else if (jsonObject.has("doors")){
            return jsonDeserializationContext.deserialize(jsonObject, Car.class);
        }
        return null;
    }

}
