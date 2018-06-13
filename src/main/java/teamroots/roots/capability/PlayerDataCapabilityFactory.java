package teamroots.roots.capability;

import java.util.concurrent.Callable;

public class PlayerDataCapabilityFactory implements Callable<IPlayerDataCapability>{

	@Override
	public IPlayerDataCapability call() throws Exception {
		return new DefaultPlayerDataCapability();
	}

}
