[![License LGPL 3](https://img.shields.io/badge/license-LGPLv3-blue.svg)](http://www.gnu.org/licenses/lgpl-3.0.txt)
![](https://reposs.herokuapp.com/?path=Ks89/SPF2)
[![Build Status](https://travis-ci.org/Ks89/SPF2.svg?branch=master)](https://travis-ci.org/Ks89/SPF2)

[![Issue Stats](http://issuestats.com/github/Ks89/SPF2/badge/pr?style=flat)](http://issuestats.com/github/Ks89/SPF2)
[![Issue Stats](http://issuestats.com/github/Ks89/SPF2/badge/issue?style=flat)](http://issuestats.com/github/Ks89/SPF2)

# SPF2CouponingProviderDemo

![alt tag](https://raw.githubusercontent.com/deib-polimi/SPF2CouponingClientDemo/master/repo_images/SPF2couponingprovider_header.png)

<br>

## Informations

This is a demo app that provides Coupons to devices with SPF and SPFCouponingClient installed on they.

SPF2CouponingProviderDemo requires Android 4.2 JellyBean MR1 (API 17) or higher. But I tested this new version only on 4.4.x KilKat, 5.x.x Lollipop and 6.0 Marshmallow. 
This choice is related to to the fact that in previous versions, Wi-Fi Direct was unstable and unreliable.

To be able to create apps based on SPF2, as this one, you must add SPFLib and SPFShared as remote dependencies into your build.gradle.

There are other 2 demo apps: 
- [SPFCouponingClient demo](https://github.com/deib-polimi/SPF2CouponingClientDemo)
- [SPFChat demo](https://github.com/deib-polimi/SPF2ChatDemo)

**This app requires that clients have SPFCouponingClient installed.**

**Main project page** [HERE](https://github.com/deib-polimi/SPF2)
**Documentation** [HERE](https://github.com/deib-polimi/SPF2_Documentation)

If you want to modify SPFLib and SPFShared, I suggest to run Sonatype Nexus OSS on your local machine 
and upload the compiled AAR's into this Maven server. Also, remember to update the version of these libraries, 
because Gradle caches dependencies automatically into .gradle/caches/modules-2/files-2.1 directory.


## Releases

- *10/27/2015* - **SPF2CouponingProviderDemo** 2.0.0 - [Download](https://github.com/Ks89/SPF2CouponingProviderDemo/releases/tag/v.2.0.0)
- *10/20/2015* - **SPF2CouponingProviderDemo** Beta 1 - [Download](https://github.com/Ks89/SPF2CouponingProviderDemo/releases/tag/v.beta1)


## Changelog

- **2.0.0**
First official release.


## Dependencies

As you can see, to be able to compile a SPF2's app, you should simply 
update your "dependencies" block inside the module's build.gradle.

```
dependencies {
compile 'it.polimi.spf:spflib:2.0.0.1@aar'
compile 'it.polimi.spf:spfshared:2.0.0.1@aar'
}
```

## Known issues

- [ ] Replace materialtabstrip library with the official version included into Design support library by Google, either for CouponingProvider or CouponingClient.
- [ ] Replace multiselection library in CouponingProvider and CouponingClient with an official version.

If you want to do something, create a Pull Request! XD


## Images

![alt tag](https://raw.githubusercontent.com/deib-polimi/SPF2CouponingProviderDemo/master/repo_images/button_iconics.png)
![alt tag](https://raw.githubusercontent.com/deib-polimi/SPF2CouponingProviderDemo/master/repo_images/drawer.png)
![alt tag](https://raw.githubusercontent.com/deib-polimi/SPF2CouponingProviderDemo/master/repo_images/drawer_proximity.png)
![alt tag](https://raw.githubusercontent.com/deib-polimi/SPF2CouponingProviderDemo/master/repo_images/notifications.png) <br />
![alt tag](https://raw.githubusercontent.com/deib-polimi/SPF2CouponingProviderDemo/master/repo_images/tablet1.png) <br />
![alt tag](https://raw.githubusercontent.com/deib-polimi/SPF2CouponingProviderDemo/master/repo_images/tablet2.png) <br />
![alt tag](https://raw.githubusercontent.com/deib-polimi/SPF2CouponingProviderDemo/master/repo_images/tablet3.png) <br />
![alt tag](https://raw.githubusercontent.com/deib-polimi/SPF2CouponingProviderDemo/master/repo_images/about_fragment.png) 


# License
Copyright 2014 Jacopo Aliprandi, Dario Archetti<br>
Copyright 2015 Stefano Cappa

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
