package speiger.src.collections.shorts.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Short2ByteConcurrentMap extends ConcurrentMap<Short, Byte>, Short2ByteMap
{
	@Override
	@Deprecated
	public default Byte compute(Short key, BiFunction<? super Short, ? super Byte, ? extends Byte> mappingFunction) {
		return Short2ByteMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Byte computeIfAbsent(Short key, Function<? super Short, ? extends Byte> mappingFunction) {
		return Short2ByteMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Byte computeIfPresent(Short key, BiFunction<? super Short, ? super Byte, ? extends Byte> mappingFunction) {
		return Short2ByteMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Short, ? super Byte> action) {
		Short2ByteMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Byte merge(Short key, Byte value, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) {
		return Short2ByteMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Byte getOrDefault(Object key, Byte defaultValue) {
		return Short2ByteMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Byte putIfAbsent(Short key, Byte value) {
		return Short2ByteMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Short2ByteMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Short key, Byte oldValue, Byte newValue) {
		return Short2ByteMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Byte replace(Short key, Byte value) {
		return Short2ByteMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Short, ? super Byte, ? extends Byte> mappingFunction) {
		Short2ByteMap.super.replaceAll(mappingFunction);
	}
}