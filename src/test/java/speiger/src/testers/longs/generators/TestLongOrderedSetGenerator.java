package speiger.src.testers.longs.generators;

import speiger.src.collections.longs.sets.LongOrderedSet;

@SuppressWarnings("javadoc")
public interface TestLongOrderedSetGenerator extends TestLongSetGenerator {
	@Override
	LongOrderedSet create(long... elements);
	@Override
	LongOrderedSet create(Object... elements);
}