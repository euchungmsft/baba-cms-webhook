# baba-cms-webhook
API service which gets alerts from Cloud Monitoring Service (CMS) and relays back

Alibaba provides event subscriptions over Messaging Service (MNS). See https://www.alibabacloud.com/help/doc-detail/28613.htm for further details. It works like below

CMS Alert -(Web POST)-> MNS Queue <-(Web API)- Consumer

But event subscription is not always found from every region. When you can't find it from your region, baba-cms-webhook helps. Alert integration is important when 

- extending alerts and spraying faults 
- legacy monitoring system and dashboard integration to manage faults
- running automatic procedures against the faults

It works like below

CMS Alert -(Web POST)-> baba-cms-webhook -(JMS)-> Consumer

It embeddes message queue on ActiveMQ with persistency. And it helps to recover the connection automatically between baba-cms-webhook and consumer. 

Requirement

1. JDK 1.8
2. Eclipse
3. Gradle 
4. Spring Boot and STS

How to install

1. Pull the repository
2. Import the project to workspace
3. Refresh Gradle project
4. See src/main/resource/application.properties and make some changes
5. Run

Configuration

in the application.properties file

1. logging.level.webhook = log level, TRACE | DEBUG | INFO | WARN | ERROR | FATAL | OFF

2. server.port = server port to listen to the alerts from CMS, ex) 8080

3. spring.activemq.broker-url = broker port which gets the connection request from consumers ex) tcp://localhost:61616

All mandates

Notes 

To integrate from CMS, goto CMS -> Alarms -> Alarm Rules, add the webhook url for example http://[your id addess]:[port]/callback

From consumer side, here's the code example to work with baba-cms-webhook

		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616"); // spring.activemq.broker-url
		factory.setTrustAllPackages(true);
	    Connection connection = factory.createConnection();
	    connection.start();
	    connection.setExceptionListener(new ExceptionListener(){

			@Override
			public void onException(JMSException ex) {
				// TODO Auto-generated method stub
				ex.printStackTrace();
			}});
	    
	    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	    Destination dest = session.createQueue("alert-queue");  //  default, don't change
	    MessageConsumer consumer = session.createConsumer(dest);
	    
	    consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				// TODO Auto-generated method stub
        //  do something




