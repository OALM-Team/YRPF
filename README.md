# YRPF Server
YRPF (YukiRolePlayFramework) is a Framework for Onset (https://playonset.com/)  
This repository is the server side of the Framework.  
This server is written in Java and need the OnsetJavaPlugin (https://github.com/OnfireNetwork/OnsetJavaPlugin)  

![YRPF Logo](https://i.imgur.com/D8A8sJn.png)

### Install the plugin on your Onset Server  
- The plugin has only been tested on Windows and Ubuntu 18
- Install openjdk 11.0.7 on your server
- Uncompress this zip in your Onset Server directory https://www.dropbox.com/s/lgjdy6y55zxmw99/release.zip?dl=0
- The zip contains the Java Plugin, Support Java libraries for Onset and the YRPF server config directory
- Add "OnsetJavaPlugin" to your server_config of Onset in the plugins node
- Add the "java" package to your server_config of Onset in the packages node
- Configure the server_config.json of YRPF in the "yrpf" directory
- Import the database from the sql file
- Install the front of the YRPF Framework (https://github.com/OALM-Team/YRPF-Client)
- Add the "yrpf" package to your server_config of Onset in the packages node
- Run your server and have fun :)

## Contribute
We are at a early stage of the Framework and the Backend is not open sourced right now, so i will not accept PR right now, but it will be available soon.
But you can help to translate for other language : https://poeditor.com/join/project/FT7CELbHb6

## Authors
Yuki https://github.com/nightwolf93

## Thanks
Talos, JanHolger, the OALM Team