package data;

import io.github.satr.aws.lambda.shoppingbot.data.RepositoryFactory;

public class NullRepositoryFactory implements RepositoryFactory {
    @Override
    public void shutdown() {
    }
}
