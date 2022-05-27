package speiger.src.testers.bytes.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.bytes.maps.abstracts.AbstractByte2ByteMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ByteMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.bytes.generators.maps.TestByte2ByteMapGenerator;
import speiger.src.testers.bytes.utils.ByteHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractByte2ByteMapTester extends AbstractObjectContainerTester<Byte2ByteMap.Entry, Byte2ByteMap>
{
	protected TestByte2ByteMapGenerator primitiveMapGenerator;

	protected Byte2ByteMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Byte, Byte>, ? extends Map.Entry<Byte, Byte>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestByte2ByteMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestByte2ByteMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Byte2ByteMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Byte2ByteMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Byte2ByteMap.Entry> getOrderedElements() {
		ObjectList<Byte2ByteMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Byte2ByteMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Byte2ByteMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Byte2ByteMap.Entry> actualContents() {
		return getMap().byte2ByteEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Byte2ByteMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Byte2ByteMap resetContainer(Byte2ByteMap newValue) {
		newValue.setDefaultReturnValue((byte)-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(byte... elements) {
		for (byte element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(byte... elements) {
		for (byte element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Byte2ByteMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Byte2ByteMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Byte2ByteMap.Entry... entries) {
		for (Byte2ByteMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getByteKey() + " mapped to value " + entry.getByteValue(), valueEquals(getMap().get(entry.getByteKey()), entry.getByteValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Byte2ByteMap.Entry> expected) {
		super.expectContents(expected);
		for (Byte2ByteMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getByteKey(), entry.getByteValue(), getMap().get(entry.getByteKey()));
		}
	}

	protected final void expectReplacement(Byte2ByteMap.Entry newEntry) {
		ObjectList<Byte2ByteMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Byte2ByteMap.Entry> expected, Byte2ByteMap.Entry newEntry) {
		for (ObjectListIterator<Byte2ByteMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (ByteHelpers.equals(i.next().getByteKey(), newEntry.getByteKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getByteKey(), expected));
	}

	private static boolean valueEquals(byte a, byte b) {
		return a == b;
	}
	
	protected Byte2ByteMap.Entry entry(byte key, byte value) {
		return new AbstractByte2ByteMap.BasicEntry(key, value);
	}
	
	protected byte[] emptyKeyArray() {
		return new byte[0];
	}
	
	protected byte[] createDisjointedKeyArray() {
		byte[] array = new byte[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected Byte[] emptyKeyObjectArray() {
		return new Byte[0];
	}
	
	protected Byte[] createDisjointedKeyObjectArray() {
		Byte[] array = new Byte[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected byte[] emptyValueArray() {
		return new byte[0];
	}
	
	protected byte[] createDisjointedValueArray() {
		byte[] array = new byte[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected Byte[] emptyValueObjectArray() {
		return new Byte[0];
	}
	
	protected Byte[] createDisjointedValueObjectArray() {
		Byte[] array = new Byte[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected byte get(byte key) {
		return getMap().get(key);
	}

	protected final byte k0() {
		return e0().getByteKey();
	}

	protected final byte v0() {
		return e0().getByteValue();
	}

	protected final byte k1() {
		return e1().getByteKey();
	}

	protected final byte v1() {
		return e1().getByteValue();
	}

	protected final byte k2() {
		return e2().getByteKey();
	}

	protected final byte v2() {
		return e2().getByteValue();
	}

	protected final byte k3() {
		return e3().getByteKey();
	}

	protected final byte v3() {
		return e3().getByteValue();
	}

	protected final byte k4() {
		return e4().getByteKey();
	}

	protected final byte v4() {
		return e4().getByteValue();
	}
}