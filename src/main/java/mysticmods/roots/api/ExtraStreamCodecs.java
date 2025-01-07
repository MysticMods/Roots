package mysticmods.roots.api;

import com.mojang.datafixers.util.Function7;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

public class ExtraStreamCodecs {
  public static <B, C, T1, T2, T3, T4, T5, T6, T7> StreamCodec<B, C> composite(
      final StreamCodec<? super B, T1> arg, final Function<C, T1> function,
      final StreamCodec<? super B, T2> arg2, final Function<C, T2> function2,
      final StreamCodec<? super B, T3> arg3, final Function<C, T3> function3,
      final StreamCodec<? super B, T4> arg4, final Function<C, T4> function4,
      final StreamCodec<? super B, T5> arg5, final Function<C, T5> function5,
      final StreamCodec<? super B, T6> arg6, final Function<C, T6> function6,
      final StreamCodec<? super B, T7> arg7, final Function<C, T7> function7,
      final Function7<T1, T2, T3, T4, T5, T6, T7, C> function72) {
    return new StreamCodec<B, C>() {

      public C decode(B object) {
        Object object2 = arg.decode(object);
        Object object3 = arg2.decode(object);
        Object object4 = arg3.decode(object);
        Object object5 = arg4.decode(object);
        Object object6 = arg5.decode(object);
        Object object7 = arg6.decode(object);
        Object object8 = arg7.decode(object);
        return function72.apply((T1) object2, (T2) object3, (T3) object4, (T4) object5, (T5) object6, (T6) object7, (T7) object8);
      }

      public void encode(B object, C object2) {
        arg.encode(object, function.apply(object2));
        arg2.encode(object, function2.apply(object2));
        arg3.encode(object, function3.apply(object2));
        arg4.encode(object, function4.apply(object2));
        arg5.encode(object, function5.apply(object2));
        arg6.encode(object, function6.apply(object2));
        arg7.encode(object, function7.apply(object2));
      }
    };
  }
}
