# YRPF Server
YRPF (YukiRolePlayFramework) is a framework for [Onset](https://playonset.com/)  
This repository is the server-side of the framework.  
This server is written in Java and requires the [OnsetJavaPlugin](https://github.com/OnfireNetwork/OnsetJavaPlugin) and [OnsetJava](https://github.com/OnfireNetwork/OnsetJava)

![YRPF Logo](https://i.imgur.com/D8A8sJn.png)

### Install the plugin on your Onset Server  
VIDEO: https://www.youtube.com/watch?v=cA_qUKJSUCU (in french) (Thanks Papy Brossard)
- The plugin has only been tested on Windows and Ubuntu 18
- Install openjdk 8 or higher on your server or Oracle JDK 8 for Windows
- Set up your JAVA_HOME env variables
- Uncompress the [release.zip](https://github.com/OALM-Team/YRPF/releases) in your Onset Server directory
- The zip contains the java plugin, support java libraries for Onset and the YRPF server config directory
- Add "OnsetJavaPlugin" to your server_config of Onset in the plugins node
- Add the "java" package to your server_config of Onset in the packages node
- Configure the server_config.json of YRPF in the "yrpf" directory
- Uncompress the [YRPF_DB.zip](https://github.com/OALM-Team/YRPF/releases)
- Import the database from the sql file
- Install the [client-side](https://github.com/OALM-Team/YRPF-Client) of the YRPF Framework
- Add the "yrpf" package to your server_config of Onset in the packages node
- Run your server and have fun :)

## Common errors
```libjvm.so: cannot open shared object file: No such file or directory```  
OnsetJava can't find the libjvm.so on your system, try this:  
https://stackoverflow.com/questions/28462302/libjvm-so-cannot-open-shared-object-file-no-such-file-or-directory  
https://askubuntu.com/questions/584529/libjvm-so-cannot-open-shared-object-file-no-such-file-or-directory  

## Contribute
You can help us to translate the framework into other languages: https://poeditor.com/join/project/FT7CELbHb6
You can also make a PR for adding new features or fixing bugs

## Authors
[Yuki](https://github.com/nightwolf93)

## Thanks
Talos, JanHolger, the OALM Team
