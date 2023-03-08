# SilentVoice
## Plugin introduction:
* This plugin can leave anonymous messages to others and tell them what you think.A small plugin.

# Note: Currently, the storage mode only supports Sqlite and MySQL.
### The plugin has three built-in languages: Chinese, English and German,If you don't have the language you want, you can also customize the language
1. Copy one from the built-in language file
2. Change its file name to the name you want
3. Change file content
4. Go to "config.yml" and change the "language" to your language file

## Below are some pictures of the plugin:
<div>
  <img src="https://github.com/plaidmrdeer/SilentVoice/blob/main/img/1.png" width="50%">
</div>
<div>
  <img src="https://github.com/plaidmrdeer/SilentVoice/blob/main/img/2.png" width="50%">
</div>

## configuration file：
```yaml
#Language Options
#The plugin has three built-in languages: Chinese, English and German
#If you don't have the language you want, you can also customize the language
#1.Copy one from the built-in language file
#2.Change its file name to the name you want
#3.Change file content
#4.Go to "config.yml" and change the "language" to your language file
language: "english"

#Plugin prefix, you can use %prefix% to prefix all text of the plugin
#You can also change it to the prefix you want
prefix: "&5&lSilentVoice &r&l>> "

#Display the plugin logo when the plugin is started
#If you don't want to display the plugin logo, you can change it to "false"
logo: true

#The database where the plugin stores data
#Currently, only Sqlite and MySQL
#The default is Sqlite. If you want to use MySQL, you can change type: "sqlite" to "mysql"
sql:
  type: "sqlite"
  #Things necessary to connect to a MySQL database
  mysql:
    #Your MySQL database address
    host: "127.0.0.1"
    #Port of your MySQL database
    port: "3306"
    #The name of your database
    database: "database"
    #User name required to connect to MySQL database
    username: "username"
    #Password required to connect to MySQL database
    password: "123456"
```

## Command:
* /svreload -- Reload configuration file
* /sv <His/her name> <Message> -- Leave a message to him/her
* /sv <His/her name> -- View his/her message

## permissions:
* SilentVoice.command.silentvoicereload:
    * default: op
    * description: Reload command permissions
* SilentVoice.command.silentvoice:
    * default: true
    * description: message menu permissions
* SilentVoice.command.silentvoice.view:
    * default: true
    * description: view message permissions
* SilentVoice.command.silentvoice.write:
    * default: true
    * description: write message permissions

# Finally, thank you for using my plug-in. If you find any errors or required functions during use, please contact me in time
