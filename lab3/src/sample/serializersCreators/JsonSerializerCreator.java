package sample.serializersCreators;

import sample.serializersCreators.serializers.JsonSerializer;

public class JsonSerializerCreator extends Creator {
    @Override
    public Serializer create(){
        return new JsonSerializer();
    }
}
