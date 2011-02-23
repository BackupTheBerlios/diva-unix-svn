Due to some strange issues in Eclipse it is quite hard to have a simple product 
that have all functions of the normal Eclipse with only some new icons, etc
For example, either I lose the ability to update the platform
or I lose part of my branding

here is some interesting pages about what I (unsuccefully) tried :
http://wiki.eclipse.org/Eclipse_RCP_How-to#Update
http://wiki.eclipse.org/Equinox_p2_director_application
http://toedter.com/blog/?p=27 

As I don't have much time for this, I fall back to another approach :
simply get the normal eclipse and patch it in some simple places : splash.bmp, ...

these simple hacks are done throught the ant script : patch_existing_eclipse_build.xml

