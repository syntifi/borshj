package com.syntifi.near.borshj;

import com.syntifi.near.borshj.annotation.BorshOrder;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.jar.asm.Opcodes;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
public class FuzzTests {
    static final int MAX_ITERATIONS = 1000;
    static final int MAX_RECURSION = 2;
    static final int MAX_FIELDS = 10;
    static final int MAX_STRING_LEN = 100;

    @Test
    void testByteBuddy() throws Exception {
        final Object bean =
                new ByteBuddy()
                        .subclass(Bean.class)
                        .defineField("name", String.class, Opcodes.ACC_PUBLIC)
                        .annotateField(AnnotationDescription.Builder.ofType(BorshOrder.class)
                                .define("order", 1)
                                .build())
                        .defineField("email", String.class, Opcodes.ACC_PUBLIC)
                        .annotateField(AnnotationDescription.Builder.ofType(BorshOrder.class)
                                .define("order", 2)
                                .build())
                        .defineField("age", Integer.class, Opcodes.ACC_PUBLIC)
                        .annotateField(AnnotationDescription.Builder.ofType(BorshOrder.class)
                                .define("order", 3)
                                .build())
                        .defineField("twitter", Boolean.class, Opcodes.ACC_PUBLIC)
                        .annotateField(AnnotationDescription.Builder.ofType(BorshOrder.class)
                                .define("order", 4)
                                .build())
                        .make()
                        .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                        .getLoaded()
                        .getDeclaredConstructor()
                        .newInstance();
        bean.getClass().getField("name").set(bean, "J. Random Hacker");
        bean.getClass().getField("email").set(bean, "jhacker@example.org");
        bean.getClass().getField("age").set(bean, 42);
        bean.getClass().getField("twitter").set(bean, true);
        assertEquals(bean, Borsh.deserialize(Borsh.serialize(bean), bean.getClass()));
    }

    @RepeatedTest(MAX_ITERATIONS)
    void testRandomBean(final RepetitionInfo test) throws Exception {
        final Random random = new Random(test.getCurrentRepetition());
        final Object bean = newRandomBean(random, 0);
        assertEquals(bean, Borsh.deserialize(Borsh.serialize(bean), bean.getClass()));
    }

    private Object newRandomBean(final Random random, final int level) throws Exception {
        DynamicType.Builder<Bean> beanBuilder = new ByteBuddy().subclass(Bean.class);

        final int fieldCount = random.nextInt(MAX_FIELDS);
        final Object[] fieldValues = new Object[fieldCount];
        for (int i = 0; i < fieldCount; i++) {
            final String fieldName = String.format("field%d", i);
            fieldValues[i] = newRandomValue(random, level);
            beanBuilder = beanBuilder.defineField(fieldName, fieldValues[i].getClass(), Opcodes.ACC_PUBLIC)
                    .annotateField(AnnotationDescription.Builder.ofType(BorshOrder.class)
                            .define("order", i + 1)
                            .build());
        }

        final Object bean = beanBuilder
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();

        for (int j = 0; j < fieldCount; j++) {
            final String fieldName = String.format("field%d", j);
            bean.getClass().getField(fieldName).set(bean, fieldValues[j]);
        }
        //System.err.println(bean.toString());  // DEBUG
        return bean;
    }

    private Object newRandomValue(final Random random, final int level) throws Exception {
        switch (Math.abs(random.nextInt()) % 10) {
            case 0:
                if (level < MAX_RECURSION) return newRandomBean(random, level + 1);
                else {/* fallthrough */}
            case 1:
                return random.nextBoolean();
            case 2:
                return (byte) random.nextInt(Byte.MAX_VALUE);
            case 3:
                return (short) random.nextInt(Short.MAX_VALUE);
            case 4:
                return random.nextInt();
            case 5:
                return random.nextLong();
            case 6:
                return BigInteger.valueOf(random.nextLong()).abs();
            case 7:
                return random.nextFloat();
            case 8:
                return random.nextDouble();
            case 9:
                return random.ints('a', 'z' + 1)
                        .limit(random.nextInt(MAX_STRING_LEN))
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();
            default:
                throw new AssertionError("unreachable");
        }
    }

    static public class Bean implements Borsh {
        @Override
        public String toString() {
            final StringBuilder buffer = new StringBuilder();
            buffer.append(this.getClass().getName());
            buffer.append('(');
            try {
                for (final Field field : this.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    buffer.append(field.getName());
                    buffer.append('=');
                    buffer.append(field.get(this));
                    buffer.append(',');
                }
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
            buffer.append(')');
            return buffer.toString();
        }

        @Override
        public boolean equals(final Object object) {
            if (object == null || object.getClass() != this.getClass()) return false;
            try {
                for (final Field field : this.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    if (!field.get(this).equals(field.get(object))) {
                        return false;
                    }
                }
                return true;
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
