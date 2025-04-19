import com.rabbitmq.client.Channel;

public abstract class Messenger {
    protected String name;
    protected String host;

    protected Channel channel;

    public Messenger(String name, String host, Channel channel){
        this.name = name;
        this.host = host;
        this.channel = channel;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        host = host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
