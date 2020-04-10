package sample.serializersCreators;

import sample.serializersCreators.serializers.CustomSerializer;

public class CustomSerializerCreator extends Creator{
    @Override
    public Serializer create(){
        return new CustomSerializer();
    }
}
