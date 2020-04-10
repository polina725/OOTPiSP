package sample.serializersCreators;

import sample.serializersCreators.serializers.BinarySerializer;

public class BinarySerializerCreator extends Creator {
    @Override
    public Serializer create(){
        return new BinarySerializer();
    }
}
