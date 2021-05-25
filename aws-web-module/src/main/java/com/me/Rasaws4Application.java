package com.me;

import com.me.aws.CredentialsStateHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

import static java.util.Objects.isNull;

@SpringBootApplication
public class Rasaws4Application {
	private static Logger log = Logger.getLogger(Rasaws4Application.class.getName());

	/**
	 * Passing AWS credentials
	 * 1 arg = aws.accessKeyId
	 * 2 arg = aws.secretKey
	 * 3 arg = aws.sns.topic.arn
	 * 4 arg = aws.sqs.queue.name
	 * Example:
	 * java -jar aws-web-module-1.0-SNAPSHOT.jar _accessKeyId_ _secretKey_
	 * where arg1="_accessKeyId_"; arg2="_secretKey_"
	 * @param args
	 */
	public static void main(final String[] args) {
		processArgs(args);
		SpringApplication.run(Rasaws4Application.class, args);
	}

	private static void processArgs(final String[] args) {
		if (isNull(args)) {
			log.warning("You have not provided any arguments!");
		} else {
			log.info("There are " + args.length + " argument(s):");
			for (String arg: args) {
				log.info(arg);
			}
			CredentialsStateHolder.setUp(args);
		}
	}

}
