divaruntime-tool.lauch launches an Equinox configuration. It deploys a causal link, a configuration checker and a GUI.

Basically this launch configuration only checks that DiVA tools deployed at runtime work properly. It is not useful as-is.

How to use the causal link with you own application?
-> open the divaruntime-tool.lauch with Run Configurations...
-> add the bundle factories (component types) needed for you application (in the Bundles tab)
-> launch this modified configuration. This will launch the causal, the checker and the GUI, as well as your factories (type ss in the console to check)
-> you can now use the GUI to deploy a configuration (assembly of component instances).