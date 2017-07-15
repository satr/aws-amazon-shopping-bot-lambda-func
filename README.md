# aws-amazon-shopping-bot-lambda-func
AWS Amazon Lambda function for the Amazon Lex Shopping-Bot

Download and unpack DynamoDBLocal to an externals folder (it is used only for unit-testing): http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.html#DynamoDBLocal.DownloadingAndRunning

Setup IAM credentials to be able to access the DynamoDB from a local computer:
http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html
Creadentials are stored in the folder (Linux, Mac):
```
~/.aws/credentials
[default]
aws_access_key_id = …
aws_secret_access_key = …
```
