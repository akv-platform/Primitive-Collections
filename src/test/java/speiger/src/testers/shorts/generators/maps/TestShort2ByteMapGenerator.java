package speiger.src.testers.shorts.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.shorts.maps.interfaces.Short2ByteMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestShort2ByteMapGenerator extends TestMapGenerator<Short, Byte> {
	public ObjectSamples<Short2ByteMap.Entry> getSamples();
	public ObjectIterable<Short2ByteMap.Entry> order(ObjectList<Short2ByteMap.Entry> insertionOrder);
	public Short2ByteMap create(Short2ByteMap.Entry... elements);
	@Override
	default Short2ByteMap create(Object... elements) {
		Short2ByteMap.Entry[] result = new Short2ByteMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Short2ByteMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Short[] createKeyArray(int length) { return new Short[length]; }
	@Override
	default Byte[] createValueArray(int length) { return new Byte[length]; }
	@Override
	default Short2ByteMap.Entry[] createArray(int length) { return new Short2ByteMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Short, Byte>> samples() {
		ObjectSamples<Short2ByteMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getShortKey(), samples.e0().getByteValue()), 
			Helpers.mapEntry(samples.e1().getShortKey(), samples.e1().getByteValue()), 
			Helpers.mapEntry(samples.e2().getShortKey(), samples.e2().getByteValue()), 
			Helpers.mapEntry(samples.e3().getShortKey(), samples.e3().getByteValue()), 
			Helpers.mapEntry(samples.e4().getShortKey(), samples.e4().getByteValue())
		);
	}
}