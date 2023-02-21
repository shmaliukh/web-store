import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionsTest {

    @Getter
    public static class TestObject {

        private long longPrimitiveValue;
        private int intPrimitiveValue;

        public void setLongPrimitiveValue(long longPrimitiveValue) {
            this.longPrimitiveValue = longPrimitiveValue;
        }

        public void setLongPrimitiveValue(Integer objectValue) {
            this.longPrimitiveValue = objectValue;
        }

        public void setLongPrimitiveValue(Long objectValue) {
            this.longPrimitiveValue = objectValue;
        }

        public void setLongPrimitiveValue(int intPrimitiveValue) {
            this.longPrimitiveValue = intPrimitiveValue;
        }

        public void setLongPrimitiveValue(char primitiveValue) {
            this.longPrimitiveValue = primitiveValue;
        }

        public void setLongPrimitiveValue(short primitiveValue) {
            this.longPrimitiveValue = primitiveValue;
        }

        public void setLongPrimitiveValue(byte primitiveValue) {
            this.longPrimitiveValue = primitiveValue;
        }

        public void setIntPrimitiveValue(int intPrimitiveValue) {
            this.intPrimitiveValue = intPrimitiveValue;
        }

        public void setIntPrimitiveValue(long primitiveValue) {
            this.intPrimitiveValue = intPrimitiveValue;
        }
    }

    private Object readValueFromSource() {
        return 1;
    }

    @Test
    void test() throws InvocationTargetException, IllegalAccessException {
        Method[] methods = TestObject.class.getMethods();
        TestObject testObject = new TestObject();

//        for (Method method : methods) {
//            if ("setLongPrimitiveValue".equals(method.getName()) && method.getParameterCount() == 1) {
//                method.invoke(testObject, 1);
//            }
//        }

        String propertyName = "longPrimitiveValue";
        Object value = readValueFromSource(); // Could be an int, Integer, char, or any other type

        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase("set" + propertyName)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1) {
                    Class<?> parameterType = parameterTypes[0];
//                    parameterType.
                    if (parameterType.isAssignableFrom(value.getClass())) {
                        method.invoke(testObject, value);
                        break;
                    }
                }
            }
        }
        System.out.println(testObject.getLongPrimitiveValue());

        Assertions.assertEquals(value, testObject.longPrimitiveValue);
    }



    @Test
    public void testWithParams(String name, Object value) {

    }
}
