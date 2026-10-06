# Changelog

## [3.0.0](https://github.com/Manuel11243/Waystones/compare/v2.3.0...v3.0.0) (2026-10-01)


### ⚠ BREAKING CHANGES

* Make use of Mojang's Brigadier command system
* Upgrade plugin to 1.21

### Feature Changes

* [#125](https://github.com/Manuel11243/Waystones/issues/125) Conditionally show compass coordinates in lore ([#126](https://github.com/Manuel11243/Waystones/issues/126)) ([5180552](https://github.com/Manuel11243/Waystones/commit/518055254a0dd47ee56a6ac58c1938fae68e638e))
* Add bossbar timer ([260756d](https://github.com/Manuel11243/Waystones/commit/260756db2305faa790996829b0f69c45c0c74444))
* Add custom serializers and better formatting to properties ([0891ccd](https://github.com/Manuel11243/Waystones/commit/0891ccdb14d6487930fa3d04297ac397b241017a))
* Add database JDBC-style repository for waystone info ([15ae8fb](https://github.com/Manuel11243/Waystones/commit/15ae8fbbd651d0e0cd4aba8b9038179185d867bd))
* Add early database support, migrate dependency loading to loader class ([8f8e378](https://github.com/Manuel11243/Waystones/commit/8f8e3789298e5d6fdc36f8caadc42c161a8c7713))
* Add early database support, migrate dependency loading to loader class ([f5bc484](https://github.com/Manuel11243/Waystones/commit/f5bc484c5418ff87802afd335b3e9f3c36bd8214))
* Add extra layer of locale de-specification ([88f3c7b](https://github.com/Manuel11243/Waystones/commit/88f3c7b0595bf491c901e75460abf2e426e5c686))
* Add formatting support for properties ([25daa5a](https://github.com/Manuel11243/Waystones/commit/25daa5ab2a1168f3b6334409f3b71e5963de17a4))
* Add list command to show known waystone data ([6b58a89](https://github.com/Manuel11243/Waystones/commit/6b58a89cb7e949cd67e9098ff4380e0cdc4ce2ac))
* Add list command to show known waystone data ([16a0f0c](https://github.com/Manuel11243/Waystones/commit/16a0f0c990b88d0c26351cd44bbd579c1993a899))
* Apply formatting to other properties ([decb877](https://github.com/Manuel11243/Waystones/commit/decb877b4eb0fe0a9bd33588d030a6810faa767a))
* Clean up command menus ([a9280fe](https://github.com/Manuel11243/Waystones/commit/a9280fed2b416989e825c1bdd4c7b11628f218cf))
* **ConfigCommand:** require permission for viewing/editing config ([bbab7ca](https://github.com/Manuel11243/Waystones/commit/bbab7ca4cfe53040b7bc1265f7927d5dcca94d64))
* **getkeyCmd:** add plural "s" if multiple keys are given ([f069436](https://github.com/Manuel11243/Waystones/commit/f06943645e9b612357e370bc84148a09125c75c6))
* **getkeyCmd:** variable first argument ([1398286](https://github.com/Manuel11243/Waystones/commit/13982868da14941c72c1c51cad6784e709672bad))
* **GetKeyCommand:** add "k" alias ([3db5564](https://github.com/Manuel11243/Waystones/commit/3db5564a9b90022cea79f2e3b313ffdf6c0a2cab))
* **GetKeyCommand:** port GetKey command ([94f9f04](https://github.com/Manuel11243/Waystones/commit/94f9f043b092d74fc1faeb8c48d72072c5ff716c))
* **givekey:** add self & other permissions ([1d07dde](https://github.com/Manuel11243/Waystones/commit/1d07dde0bcb7dd240cf2e055eae44f3f387c1ecd))
* Implement new localization manager ([7e980a9](https://github.com/Manuel11243/Waystones/commit/7e980a9ae7e90300be67955c0b6b8a58dd713fb1))
* Implement plugin update check command and caching mechanism ([#140](https://github.com/Manuel11243/Waystones/issues/140)) ([cf892b9](https://github.com/Manuel11243/Waystones/commit/cf892b94966b270f152368af09b6116f51938c89))
* **InfoCmd:** display version number ([95689fc](https://github.com/Manuel11243/Waystones/commit/95689fcce391b14ed15c511675d2d2307aa71a77))
* **InfoCommand:** add "i" alias ([2dac517](https://github.com/Manuel11243/Waystones/commit/2dac51786c91b9dc4c723988a0c2b3c006173f4c))
* **InfoCommand:** port Info command ([9210ecf](https://github.com/Manuel11243/Waystones/commit/9210ecfe38fe15e3e1a0e5457055ce13ecfcbe34))
* **MainCmd:** command manager ([9b34b14](https://github.com/Manuel11243/Waystones/commit/9b34b141d2f767a752e5bec0e134922a04389a61))
* Make use of Mojang's Brigadier command system ([b70c3c7](https://github.com/Manuel11243/Waystones/commit/b70c3c7e309bcfee99635bf5303f9ba0cb757599))
* **ownership:** Add schema and config for waystone ownership ([#145](https://github.com/Manuel11243/Waystones/issues/145)) ([c97d5f5](https://github.com/Manuel11243/Waystones/commit/c97d5f555dac9e6fd2fbfd55e961b0329d719367))
* Reimplement world ratio system ([13342db](https://github.com/Manuel11243/Waystones/commit/13342db6b9468480bc7b8c53f21455e358683933))
* **WarpEvent:** display message and fizzle out when cancelled ([ddbd95a](https://github.com/Manuel11243/Waystones/commit/ddbd95a25e4efcc6e95e7be7bf2778c3540bfd4b))
* **WarpKeyCmd:** add command to give WarpKeys to players ([0e12fa6](https://github.com/Manuel11243/Waystones/commit/0e12fa6b21c787c731cea3d5a62ac581d641ee85))
* **WarpstoneCommand:** add shorter command alias ([63e8cb0](https://github.com/Manuel11243/Waystones/commit/63e8cb03f20ce005442d8b8fd1d9e799ebb93c0f))
* **WarpstoneCommand:** giveKey aliases ([bdfddc5](https://github.com/Manuel11243/Waystones/commit/bdfddc5e2a3efe791d91e740180a2e4b1417433f))


### Bug Fixes

* [#136](https://github.com/Manuel11243/Waystones/issues/136) Add more robust block safety checks ([#137](https://github.com/Manuel11243/Waystones/issues/137)) ([8e937a2](https://github.com/Manuel11243/Waystones/commit/8e937a2e245aad0e3b073bcbcfda5fe0883c8bde))
* Allow waystone keys to be relinked if the waystone name has changed ([8dc2d58](https://github.com/Manuel11243/Waystones/commit/8dc2d58d250008522d75da4bbc9e5dd131b0a63d))
* **CommandNamespace:** inform sender if command is unknown ([b0b90f9](https://github.com/Manuel11243/Waystones/commit/b0b90f9743a8b0428f27057731e56e944e0ba13b))
* Database mapping logic ([fda6bd5](https://github.com/Manuel11243/Waystones/commit/fda6bd53e9e9df5d80cf72dd2528f33bd999fc5d))
* Distance limitations and localization ([b9511b3](https://github.com/Manuel11243/Waystones/commit/b9511b3d41ddf6caea3dbcc48333292355d8b594))
* Distance limitations and localization ([80cf86d](https://github.com/Manuel11243/Waystones/commit/80cf86d1268f25fd5059811f82c1bea9674c1511))
* Fix background texture for Waystones advancement window ([653778e](https://github.com/Manuel11243/Waystones/commit/653778e0b41850053b77269ebb53200cb4f7cb40))
* Fix background texture for Waystones advancement window ([791fda0](https://github.com/Manuel11243/Waystones/commit/791fda04bed09cf5483112beaf7d548ffe7bc013))
* Fix config serialization issue ([b157cfc](https://github.com/Manuel11243/Waystones/commit/b157cfc687b3f7637aef6fbf2499d9a067fba872))
* **getkeyCmd:** sender's name in cmd result message ([79f9459](https://github.com/Manuel11243/Waystones/commit/79f9459cb9b5ae915961ff9f3bb00b8c59bb139a))
* **GetKeyCommand:** formatting and permission error message ([01d4aef](https://github.com/Manuel11243/Waystones/commit/01d4aeff1ce23448eeb42b495061d74b48181a91))
* Include missing Jukebox block, fix chest detection ([5f99b8d](https://github.com/Manuel11243/Waystones/commit/5f99b8d58267e6131191440614f6a986d216b1e2))
* Include missing Jukebox block, fix chest detection ([c45f473](https://github.com/Manuel11243/Waystones/commit/c45f47356f68e0154d4894110a99c80eb0ff019a))
* Include permission checks ([2f5f290](https://github.com/Manuel11243/Waystones/commit/2f5f290ea1afe68c21854a9bd0e6e3962328552e))
* **InfoCmd:** remove references to reload command ([96c7796](https://github.com/Manuel11243/Waystones/commit/96c77960e10b386d6fdfd953095fbe5c545ab53b))
* **InfoCommand:** add missing trans rights ([d90e904](https://github.com/Manuel11243/Waystones/commit/d90e904b6e2be226d69eddf23b11ae26e3a70837))
* Introduce oxidized copper blocks to boost block mappings, add optimized boost for waxed unoxidized copper blocks ([15a6f25](https://github.com/Manuel11243/Waystones/commit/15a6f251a7f0db167711f98c364a2c53308474ac))
* Issue with config command breaking due to missing property info ([42fa83d](https://github.com/Manuel11243/Waystones/commit/42fa83d5e65fb5489a007bf864255de37acccac7))
* Key command can be executed in invalid ways ([#132](https://github.com/Manuel11243/Waystones/issues/132)) ([56e928d](https://github.com/Manuel11243/Waystones/commit/56e928d181af9f7e62f44ad238831e41d347a8a8))
* Linking issue localization formatting ([6b9d550](https://github.com/Manuel11243/Waystones/commit/6b9d550bb36cdeedf474afc421182a9c01b11883))
* Lower volume of some sounds ([4e86519](https://github.com/Manuel11243/Waystones/commit/4e86519114704b7f7babad653b8bab3313be8a59))
* Lower volume of some sounds ([f8fff13](https://github.com/Manuel11243/Waystones/commit/f8fff13afc171c1e0c04cd2dae3b29466f1417fa))
* Name overwrite issue on key linking ([59cea25](https://github.com/Manuel11243/Waystones/commit/59cea25039f665036f9e0f3b106b15ebc1110b56))
* New empty list message for page one ([a4aa849](https://github.com/Manuel11243/Waystones/commit/a4aa849ed1a1aedcda85f74b2fd49648242612a5))
* New empty list message for page one ([05a86d8](https://github.com/Manuel11243/Waystones/commit/05a86d84fe56a39964dd4f9d81102f2731d3afcd))
* **plugin.yml:** add missing permissions ([1c1ad81](https://github.com/Manuel11243/Waystones/commit/1c1ad81dca604faae19fe0bf6dff82a62edb7209))
* Portal sickness chance inversion ([98e50cd](https://github.com/Manuel11243/Waystones/commit/98e50cdf9f701c8d65836c014891dac903d53e35))
* Prevent naming a waystone if a nametag has the same name as the waystone ([6feddf7](https://github.com/Manuel11243/Waystones/commit/6feddf792802fbec2fad95989958d1c8c99af5cb))
* Properly retrieve git pipeline build hash ([7f07d9b](https://github.com/Manuel11243/Waystones/commit/7f07d9b8a18ca589c0e6f987914fba204a6adaf3))
* Properly retrieve git pipeline build hash ([c0f304d](https://github.com/Manuel11243/Waystones/commit/c0f304da37b2a35e0da5b949a47eab302e549062))
* Remove implicit List&lt;T&gt; return on DestroyEvent event listeners ([19038e8](https://github.com/Manuel11243/Waystones/commit/19038e8546d2d2bd88e6946b010de5c62d20d6a8))
* Resolve de-syncing issue with inventories on link ([d20cbf9](https://github.com/Manuel11243/Waystones/commit/d20cbf99a58929d201f67091166c862ad57aa28a))
* Resolve de-syncing issue with inventories on link ([b41bc3b](https://github.com/Manuel11243/Waystones/commit/b41bc3b7ce21f0bb099976bf60c53bc3800d6b83))
* Severed waystone message, missing waystone names ([ecf673f](https://github.com/Manuel11243/Waystones/commit/ecf673f0c3c88b2a6de342e04829ab6eed7b72f1))
* Shrink plugin size, disable snapshot config caching ([#138](https://github.com/Manuel11243/Waystones/issues/138)) ([d76566e](https://github.com/Manuel11243/Waystones/commit/d76566e47712be397d6f1700b41898c60c13c12c))
* Update parsing of UUID values for MySQL ([1fedfb9](https://github.com/Manuel11243/Waystones/commit/1fedfb9372fa79e966e38c359d9b5bc1fc8ad7b5))
* Various waystone naming bugs ([33e0b61](https://github.com/Manuel11243/Waystones/commit/33e0b6190a5afb3f5078a18333380e70cde8f371))
* **WarpEvent:** sounds and message triggering when damaged at all ([45db3e3](https://github.com/Manuel11243/Waystones/commit/45db3e31eff3e4de9789c81d58b74c7e86e50eeb))
* **WarpstoneCommand:** avoid case sensitive issues ([0a7e80b](https://github.com/Manuel11243/Waystones/commit/0a7e80bd75723159281a3d2be24ed25b7caa6b54))


### Code Refactors

* **commands:** improve visual styling ([da3866b](https://github.com/Manuel11243/Waystones/commit/da3866b14007fe8b6c13f55c867574e87f01200d))
* **ConfigCommand:** cleanup perm check ([60e9b45](https://github.com/Manuel11243/Waystones/commit/60e9b45588999a49b9df43187ae240a88931f7a7))
* **getkeyCmd:** improve ItemStack amount ([390dcaf](https://github.com/Manuel11243/Waystones/commit/390dcaf21c9aed5ddc4aa660f1c901724f67942c))
* **getkeyCmd:** make name and perms more consistent ([55ed1b7](https://github.com/Manuel11243/Waystones/commit/55ed1b70c89a000dbd4d90c64adb398538e09240))
* **getkeyCmd:** rename warpKeyCmd to be more descriptive ([b272dd8](https://github.com/Manuel11243/Waystones/commit/b272dd8c35fb77dd0406f5cdce8a7e29d424abff))
* **getkeyCmd:** rework permission name and checking ([7ca6bd2](https://github.com/Manuel11243/Waystones/commit/7ca6bd2ae78c3c53f536647462f4bfe94b37c37c))
* **infoCmd:** improve getkeyCmd usage info ([491229f](https://github.com/Manuel11243/Waystones/commit/491229fb2a69ccaaeae44f3a70b6f12f58ff930b))
* **infoCmd:** improve plugin reference ([8105171](https://github.com/Manuel11243/Waystones/commit/810517112e62151eba443ca893a56c324a47911a))
* **InfoCmd:** remove unnecessary logger ([0fff8c7](https://github.com/Manuel11243/Waystones/commit/0fff8c72077130ac00ec136e3cabf8dd48e7ca97))
* **infoCmd:** single multiline message ([7bc4c61](https://github.com/Manuel11243/Waystones/commit/7bc4c6162c2da88f5f904c420b1167c82cc86124))
* **InfoCommand:** improve format & show contributors ([bcc6bb8](https://github.com/Manuel11243/Waystones/commit/bcc6bb8bcd114a2a1700bcd81ee8cf3ba9d63b4e))
* **plugin.yml:** improve command info ([009aacf](https://github.com/Manuel11243/Waystones/commit/009aacfb80cfd8f35348b4467ee048670ac294e8))
* **plugin.yml:** remove reload perm from waystones.* ([ad463fc](https://github.com/Manuel11243/Waystones/commit/ad463fc3320bb0b3f9e0254886ef88e3ed8fa883))
* **StringUtils:** improve pluralizing method ([b6bdca1](https://github.com/Manuel11243/Waystones/commit/b6bdca1db4577a95d1deca83ee69d7ca421da69a))
* Upgrade plugin to 1.21 ([33f2940](https://github.com/Manuel11243/Waystones/commit/33f294093bd0f5103c8ab4436c9f34f4482e8bbf))
* **WarpEvent:** reword ActionError messages ([1bbd1f5](https://github.com/Manuel11243/Waystones/commit/1bbd1f54208c6cad93a7164e73926a31e1862da3))
* **WarpKeyCmd:** simplify amt/player declarations ([6ceba73](https://github.com/Manuel11243/Waystones/commit/6ceba73856ec4ab4d3540a3fe71a5690f1ecabc5))
* **WarpKeyCmd:** use ItemStack with amt ([fc6f54d](https://github.com/Manuel11243/Waystones/commit/fc6f54d72db62a803ba8511378eabe94a424f0b1))
* **WarpstoneCommand:** color codes in chat messages ([eee3914](https://github.com/Manuel11243/Waystones/commit/eee3914537743b8ce6920e2d6294468d3e4dd14e))
* **WarpstoneCommand:** improve CommandExecutor argument names ([922465d](https://github.com/Manuel11243/Waystones/commit/922465d49c771b30d82e76ed7d70507e0531f796))
* **WarpstoneCommand:** merge cmd functions into main class ([817eb67](https://github.com/Manuel11243/Waystones/commit/817eb672d7ed39ba3ef94ee554d67b9de682e98a))
* **WarpstoneCommand:** move getPlayerAndAmount to class scope ([0f31f2b](https://github.com/Manuel11243/Waystones/commit/0f31f2b508caf33ad7d8da92f984e6adbf395d83))
* **WarpstoneCommand:** remove unnecessary out modifier ([c6565e7](https://github.com/Manuel11243/Waystones/commit/c6565e7c3c1d13dc3b0dfcd8a5268eb5f9fbd643))
* **WarpstoneCommand:** rename amount variable ([b6badc9](https://github.com/Manuel11243/Waystones/commit/b6badc927cbb8f6c4596f0437c3ad734ca8f04b8))
* **Waystones:** rename main command ([faf62bb](https://github.com/Manuel11243/Waystones/commit/faf62bbdd7926ace0449e9bb3a38c60680551e0a))


### Miscellaneous Changes

* Add admin permission to paper-plugin.yml ([b46a058](https://github.com/Manuel11243/Waystones/commit/b46a05803129516ffc4b37ee1f6123b218006003))
* Add disabled text to ratio list ([8762bd1](https://github.com/Manuel11243/Waystones/commit/8762bd116f369bdb5f8b150f08a9d37722537f32))
* Add early bStats support ([aafc799](https://github.com/Manuel11243/Waystones/commit/aafc799217026451bfa3b686a661e84a3646becd))
* Add early bStats support ([e85133e](https://github.com/Manuel11243/Waystones/commit/e85133e2a9800a731756ec727e8be4df2ce6c017))
* Add extra auto-generated folders to .gitignore ([8ba5017](https://github.com/Manuel11243/Waystones/commit/8ba5017ba8854445cbe3e71569a8cb70a2e81d86))
* Add new config fields for database information ([428ea02](https://github.com/Manuel11243/Waystones/commit/428ea027a0dbb37dd92c66028bb9b7bfed904d4c))
* Add temporary migration command for waystone names ([8af6796](https://github.com/Manuel11243/Waystones/commit/8af67962dbfd36f472b32319a7ec6a0a4c8c9187))
* Add test dependencies for later use, update plugin.yml ([abd7df7](https://github.com/Manuel11243/Waystones/commit/abd7df7f1b51fc2b3759f9d0996efd61b29f6c5d))
* Apply detekt linting ([e4fbb43](https://github.com/Manuel11243/Waystones/commit/e4fbb43f089b52faa69fe7f23a3473018ea5f123))
* Bump plugin version to 1.21.11 ([8bca086](https://github.com/Manuel11243/Waystones/commit/8bca086191e9653da61146d396ebb0c914ba99f8))
* Bump plugin version to 1.21.11 ([8b2c5be](https://github.com/Manuel11243/Waystones/commit/8b2c5be6daeb7952c26e2c877d06d22439db1524))
* Clean up and move around utility functions ([0267213](https://github.com/Manuel11243/Waystones/commit/02672135ccae9f5ad2a7f4e7678f8fe4af3ce8ad))
* Clean up main initialization logic ([ecea946](https://github.com/Manuel11243/Waystones/commit/ecea94608da75f7fb3bc6f64c51df7a11606992f))
* Clean up remaining localization calls ([c2510e6](https://github.com/Manuel11243/Waystones/commit/c2510e65e507893b639c0ff15733715c96d1388d))
* Clean up teleport logic further ([b8670e9](https://github.com/Manuel11243/Waystones/commit/b8670e91cf14329cf94b43058eafcd69e2f4ac07))
* Clean up utility files a bit ([3db3852](https://github.com/Manuel11243/Waystones/commit/3db3852050c6fa62ff5614739f8a155258108d00))
* Clean up WaystoneService, move teleport logic to TeleportService ([a13f536](https://github.com/Manuel11243/Waystones/commit/a13f5361f1d899e3c8d205d7b13cecf8f93a6e70))
* Convert more objects to classes and inject via Koin ([c1c7ec2](https://github.com/Manuel11243/Waystones/commit/c1c7ec230654c35c8f4d3cb7825a3b838cc9453e))
* Convert more objects to Di components ([c105762](https://github.com/Manuel11243/Waystones/commit/c105762ab27d6797bd7210fe48434c2bd7fdeef7))
* Delete legacy Json class file ([83b2c46](https://github.com/Manuel11243/Waystones/commit/83b2c46f518b11f139263ac9804f791ff97d0ad1))
* Fix broken advancements ([ab87d56](https://github.com/Manuel11243/Waystones/commit/ab87d561505c7cf47265fb5b5259f7eed47699ac))
* Fix detekt issues ([e8cb548](https://github.com/Manuel11243/Waystones/commit/e8cb54862e4520c096597af50317b80c7af02df9))
* Fix detekt linting issues ([f06bf91](https://github.com/Manuel11243/Waystones/commit/f06bf9161ec35cd17635f02f46360126efdeab35))
* Further utility cleanup, fully remove old config system ([87dab4d](https://github.com/Manuel11243/Waystones/commit/87dab4d2236b0b2c135e1f2574c159cb82ee638f))
* Implement properties as dependencies instead of using global config, switch out KeyHandler for KeyService ([ca1004b](https://github.com/Manuel11243/Waystones/commit/ca1004baddb2ea564f57c71b72045bb1bc7a3208))
* Improve bStats tracking and move metrics to a dedicated module ([#142](https://github.com/Manuel11243/Waystones/issues/142)) ([35bfc11](https://github.com/Manuel11243/Waystones/commit/35bfc1103d79ea912956cbfec005869078caa977))
* Improve item renaming via anvils ([d598cbf](https://github.com/Manuel11243/Waystones/commit/d598cbfff2c17bbdde23a46afe78d0505a6f4199))
* Improve item renaming via anvils ([dccb415](https://github.com/Manuel11243/Waystones/commit/dccb41525a7ef9cbbd01472fe6e39616ef98f87a))
* Improve migration function safety ([3156c52](https://github.com/Manuel11243/Waystones/commit/3156c52e3917148c8740d7d130c8f7ccebf5a052))
* Improve warp validation for waystone keys ([64b0f72](https://github.com/Manuel11243/Waystones/commit/64b0f72912f20d5d4930cde154ecb935aa105190))
* Improve warp validation for waystone keys ([f203757](https://github.com/Manuel11243/Waystones/commit/f203757834c35dfa2c98d1ccf329788284614f8c))
* Initialize events via dedicated EventManager class ([3d86810](https://github.com/Manuel11243/Waystones/commit/3d86810a544702b69b731f9468da76a4c8e01cef))
* Introduce Detekt, lint project ([767efa4](https://github.com/Manuel11243/Waystones/commit/767efa4768561625f56baa3311298d6d1db1214b))
* Introduce Detekt, lint project ([bafd75e](https://github.com/Manuel11243/Waystones/commit/bafd75e0ff90acc7749a0b4aa450d2ea7e6f4d3a))
* Introduce issue templates ([bf80bf5](https://github.com/Manuel11243/Waystones/commit/bf80bf56afca116603f22f2bee3f1cee14039a2c))
* Introduce issue templates ([156b7bd](https://github.com/Manuel11243/Waystones/commit/156b7bd94d12b1b090d21c36c151e8c9c28a7f1d))
* **main:** release 2.1.4 ([#118](https://github.com/Manuel11243/Waystones/issues/118)) ([5d9cda0](https://github.com/Manuel11243/Waystones/commit/5d9cda0f618b3016c141d03f2f74741513453c09))
* **main:** release 2.1.5 ([#124](https://github.com/Manuel11243/Waystones/issues/124)) ([7f21810](https://github.com/Manuel11243/Waystones/commit/7f2181005e34a745ae0de63edd03319ed38abea0))
* **main:** release 2.2.0 ([#127](https://github.com/Manuel11243/Waystones/issues/127)) ([d7a1307](https://github.com/Manuel11243/Waystones/commit/d7a13072487001668ee666b5882646f286d502b5))
* **main:** release 2.2.1 ([#133](https://github.com/Manuel11243/Waystones/issues/133)) ([214bcbc](https://github.com/Manuel11243/Waystones/commit/214bcbce7f5e276fc93e99a419ef8c5db7f81dab))
* **main:** release 2.2.2 ([#139](https://github.com/Manuel11243/Waystones/issues/139)) ([2cb1c44](https://github.com/Manuel11243/Waystones/commit/2cb1c4420c3f6232cf1491fc6e34bc3e4d989860))
* **main:** release 2.3.0 ([#141](https://github.com/Manuel11243/Waystones/issues/141)) ([ca32d0b](https://github.com/Manuel11243/Waystones/commit/ca32d0bf6905f2edcae922357fb70b475b9abb09))
* **master:** release 2.0.0 ([1d5d4ff](https://github.com/Manuel11243/Waystones/commit/1d5d4ff5e2a1a8851d7e88e8532aec0d9b5037d6))
* **master:** release 2.0.0 ([e0c1eb7](https://github.com/Manuel11243/Waystones/commit/e0c1eb7690494fffb3c636e1ffd7d3759ee404ef))
* **master:** release 2.1.0 ([9e8c47c](https://github.com/Manuel11243/Waystones/commit/9e8c47c126947ed28283a65a7d6d2ca1a3ea6edf))
* **master:** release 2.1.0 ([5588f01](https://github.com/Manuel11243/Waystones/commit/5588f01243dacb52af81e3f88dc44f43b0d7b33d))
* **master:** release 2.1.1 ([1917e2c](https://github.com/Manuel11243/Waystones/commit/1917e2c214fa4535721d2f5e5c54cddb7e2ef762))
* **master:** release 2.1.1 ([df7c498](https://github.com/Manuel11243/Waystones/commit/df7c498bd6395b381d16711759eaec67838eeb76))
* **master:** release 2.1.2 ([81db41c](https://github.com/Manuel11243/Waystones/commit/81db41c06ac847da0220f9209faa04ade66cb1e9))
* **master:** release 2.1.2 ([7e4111e](https://github.com/Manuel11243/Waystones/commit/7e4111e00ce1c0c1507e9c6a3d2912d457e1eb96))
* **master:** release 2.1.3 ([4b3395a](https://github.com/Manuel11243/Waystones/commit/4b3395ab7cdcece1b59970adb60719405e52cc36))
* **master:** release 2.1.3 ([37899a0](https://github.com/Manuel11243/Waystones/commit/37899a0db02ce9848ec775ec31418fdbe4ef1675))
* Minimize plugin shadowing ([60206cf](https://github.com/Manuel11243/Waystones/commit/60206cf916bef88239e381919c8be6e9557ebab0))
* Minimize plugin shadowing ([9da5194](https://github.com/Manuel11243/Waystones/commit/9da5194f6eb017e4e888ad2e4823771560a55547))
* More cleanup of old references ([4056b81](https://github.com/Manuel11243/Waystones/commit/4056b813607ee272635bfb48bac3177daf0bdc5b))
* Move files around, make provider for default key ([408def8](https://github.com/Manuel11243/Waystones/commit/408def86e7cab44585ff0ad313d6afa25b3388cb))
* Move Power and SicknessOption enums to property directory ([d4e6176](https://github.com/Manuel11243/Waystones/commit/d4e6176dc178582e19cfe02004e5925d9e1fe937))
* Remove some calls from legacy localization property ([65a4aa0](https://github.com/Manuel11243/Waystones/commit/65a4aa0555d3db1b5a3f5ead9493f18125178fe7))
* Revamp advancement management ([3c7a3a6](https://github.com/Manuel11243/Waystones/commit/3c7a3a6511db22eec84d4a56eed1d83bd10aa719))
* Simplify arrow fold function calls ([c98a56f](https://github.com/Manuel11243/Waystones/commit/c98a56fea84bd17278f8487816308a134eb99f1f))
* Update plugin to 1.21.7 ([9cfe480](https://github.com/Manuel11243/Waystones/commit/9cfe480ffab40c95992e5ceaf9cdc4fee6aba293))
* Update plugin to 1.21.7 ([3ae147e](https://github.com/Manuel11243/Waystones/commit/3ae147e92348b9fc209f16425238870ea02e5fcb))
* Update plugin to 1.21.8 ([cb5ddf1](https://github.com/Manuel11243/Waystones/commit/cb5ddf1508f81c226c671c5c3e28a96b3065b34d))
* Update plugin to 1.21.8 ([0a1d590](https://github.com/Manuel11243/Waystones/commit/0a1d59066b70a09331c7e34bc6c4b2fd63f975a7))
* Update property formatting ([048fcd3](https://github.com/Manuel11243/Waystones/commit/048fcd37d76de0632120c59092a4087290fe2a55))
* Update README.md ([a201bb5](https://github.com/Manuel11243/Waystones/commit/a201bb5579e022a4ad17ac52023f8356767798e0))
* Update Waystones to MC 26.1.2 ([#123](https://github.com/Manuel11243/Waystones/issues/123)) ([eff1b32](https://github.com/Manuel11243/Waystones/commit/eff1b321287fca1a98a7e5724fb425dc8dc29cbf))

## [2.3.0](https://github.com/AtriusX/Waystones/compare/v2.2.2...v2.3.0) (2026-06-02)


### Feature Changes

* Implement plugin update check command and caching mechanism ([#140](https://github.com/AtriusX/Waystones/issues/140)) ([cf892b9](https://github.com/AtriusX/Waystones/commit/cf892b94966b270f152368af09b6116f51938c89))


### Miscellaneous Changes

* Improve bStats tracking and move metrics to a dedicated module ([#142](https://github.com/AtriusX/Waystones/issues/142)) ([35bfc11](https://github.com/AtriusX/Waystones/commit/35bfc1103d79ea912956cbfec005869078caa977))

## [2.2.2](https://github.com/AtriusX/Waystones/compare/v2.2.1...v2.2.2) (2026-05-22)


### Bug Fixes

* Shrink plugin size, disable snapshot config caching ([#138](https://github.com/AtriusX/Waystones/issues/138)) ([d76566e](https://github.com/AtriusX/Waystones/commit/d76566e47712be397d6f1700b41898c60c13c12c))

## [2.2.1](https://github.com/AtriusX/Waystones/compare/v2.2.0...v2.2.1) (2026-05-22)


### Bug Fixes

* [#136](https://github.com/AtriusX/Waystones/issues/136) Add more robust block safety checks ([#137](https://github.com/AtriusX/Waystones/issues/137)) ([8e937a2](https://github.com/AtriusX/Waystones/commit/8e937a2e245aad0e3b073bcbcfda5fe0883c8bde))
* Key command can be executed in invalid ways ([#132](https://github.com/AtriusX/Waystones/issues/132)) ([56e928d](https://github.com/AtriusX/Waystones/commit/56e928d181af9f7e62f44ad238831e41d347a8a8))

## [2.2.0](https://github.com/AtriusX/Waystones/compare/v2.1.5...v2.2.0) (2026-05-14)


### Feature Changes

* [#125](https://github.com/AtriusX/Waystones/issues/125) Conditionally show compass coordinates in lore ([#126](https://github.com/AtriusX/Waystones/issues/126)) ([5180552](https://github.com/AtriusX/Waystones/commit/518055254a0dd47ee56a6ac58c1938fae68e638e))

## [2.1.5](https://github.com/AtriusX/Waystones/compare/v2.1.4...v2.1.5) (2026-04-24)


### Miscellaneous Changes

* Update Waystones to MC 26.1.2 ([#123](https://github.com/AtriusX/Waystones/issues/123)) ([eff1b32](https://github.com/AtriusX/Waystones/commit/eff1b321287fca1a98a7e5724fb425dc8dc29cbf))

## [2.1.4](https://github.com/AtriusX/Waystones/compare/v2.1.3...v2.1.4) (2026-02-08)


### Bug Fixes

* Allow waystone keys to be relinked if the waystone name has changed ([8dc2d58](https://github.com/AtriusX/Waystones/commit/8dc2d58d250008522d75da4bbc9e5dd131b0a63d))
* Include missing Jukebox block, fix chest detection ([5f99b8d](https://github.com/AtriusX/Waystones/commit/5f99b8d58267e6131191440614f6a986d216b1e2))
* Include missing Jukebox block, fix chest detection ([c45f473](https://github.com/AtriusX/Waystones/commit/c45f47356f68e0154d4894110a99c80eb0ff019a))
* Prevent naming a waystone if a nametag has the same name as the waystone ([6feddf7](https://github.com/AtriusX/Waystones/commit/6feddf792802fbec2fad95989958d1c8c99af5cb))
* Various waystone naming bugs ([33e0b61](https://github.com/AtriusX/Waystones/commit/33e0b6190a5afb3f5078a18333380e70cde8f371))


### Miscellaneous Changes

* Add early bStats support ([aafc799](https://github.com/AtriusX/Waystones/commit/aafc799217026451bfa3b686a661e84a3646becd))
* Add early bStats support ([e85133e](https://github.com/AtriusX/Waystones/commit/e85133e2a9800a731756ec727e8be4df2ce6c017))
* Improve warp validation for waystone keys ([64b0f72](https://github.com/AtriusX/Waystones/commit/64b0f72912f20d5d4930cde154ecb935aa105190))
* Improve warp validation for waystone keys ([f203757](https://github.com/AtriusX/Waystones/commit/f203757834c35dfa2c98d1ccf329788284614f8c))

## [2.1.3](https://github.com/AtriusX/Waystones/compare/v2.1.2...v2.1.3) (2026-01-28)


### Bug Fixes

* Introduce oxidized copper blocks to boost block mappings, add optimized boost for waxed unoxidized copper blocks ([15a6f25](https://github.com/AtriusX/Waystones/commit/15a6f251a7f0db167711f98c364a2c53308474ac))


### Miscellaneous Changes

* Update README.md ([a201bb5](https://github.com/AtriusX/Waystones/commit/a201bb5579e022a4ad17ac52023f8356767798e0))

## [2.1.2](https://github.com/AtriusX/Waystones/compare/v2.1.1...v2.1.2) (2026-01-26)


### Bug Fixes

* Lower volume of some sounds ([4e86519](https://github.com/AtriusX/Waystones/commit/4e86519114704b7f7babad653b8bab3313be8a59))
* Lower volume of some sounds ([f8fff13](https://github.com/AtriusX/Waystones/commit/f8fff13afc171c1e0c04cd2dae3b29466f1417fa))

## [2.1.1](https://github.com/AtriusX/Waystones/compare/v2.1.0...v2.1.1) (2026-01-20)


### Bug Fixes

* Remove implicit List&lt;T&gt; return on DestroyEvent event listeners ([19038e8](https://github.com/AtriusX/Waystones/commit/19038e8546d2d2bd88e6946b010de5c62d20d6a8))


### Miscellaneous Changes

* Bump plugin version to 1.21.11 ([8bca086](https://github.com/AtriusX/Waystones/commit/8bca086191e9653da61146d396ebb0c914ba99f8))
* Bump plugin version to 1.21.11 ([8b2c5be](https://github.com/AtriusX/Waystones/commit/8b2c5be6daeb7952c26e2c877d06d22439db1524))

## [2.1.0](https://github.com/AtriusX/Waystones/compare/v2.0.0...v2.1.0) (2025-10-09)


### Feature Changes

* Add database JDBC-style repository for waystone info ([15ae8fb](https://github.com/AtriusX/Waystones/commit/15ae8fbbd651d0e0cd4aba8b9038179185d867bd))
* Add early database support, migrate dependency loading to loader class ([8f8e378](https://github.com/AtriusX/Waystones/commit/8f8e3789298e5d6fdc36f8caadc42c161a8c7713))
* Add early database support, migrate dependency loading to loader class ([f5bc484](https://github.com/AtriusX/Waystones/commit/f5bc484c5418ff87802afd335b3e9f3c36bd8214))
* Add list command to show known waystone data ([6b58a89](https://github.com/AtriusX/Waystones/commit/6b58a89cb7e949cd67e9098ff4380e0cdc4ce2ac))
* Add list command to show known waystone data ([16a0f0c](https://github.com/AtriusX/Waystones/commit/16a0f0c990b88d0c26351cd44bbd579c1993a899))


### Bug Fixes

* Database mapping logic ([fda6bd5](https://github.com/AtriusX/Waystones/commit/fda6bd53e9e9df5d80cf72dd2528f33bd999fc5d))
* Name overwrite issue on key linking ([59cea25](https://github.com/AtriusX/Waystones/commit/59cea25039f665036f9e0f3b106b15ebc1110b56))
* New empty list message for page one ([a4aa849](https://github.com/AtriusX/Waystones/commit/a4aa849ed1a1aedcda85f74b2fd49648242612a5))
* New empty list message for page one ([05a86d8](https://github.com/AtriusX/Waystones/commit/05a86d84fe56a39964dd4f9d81102f2731d3afcd))
* Resolve de-syncing issue with inventories on link ([d20cbf9](https://github.com/AtriusX/Waystones/commit/d20cbf99a58929d201f67091166c862ad57aa28a))
* Resolve de-syncing issue with inventories on link ([b41bc3b](https://github.com/AtriusX/Waystones/commit/b41bc3b7ce21f0bb099976bf60c53bc3800d6b83))
* Update parsing of UUID values for MySQL ([1fedfb9](https://github.com/AtriusX/Waystones/commit/1fedfb9372fa79e966e38c359d9b5bc1fc8ad7b5))


### Miscellaneous Changes

* Add admin permission to paper-plugin.yml ([b46a058](https://github.com/AtriusX/Waystones/commit/b46a05803129516ffc4b37ee1f6123b218006003))
* Add extra auto-generated folders to .gitignore ([8ba5017](https://github.com/AtriusX/Waystones/commit/8ba5017ba8854445cbe3e71569a8cb70a2e81d86))
* Add new config fields for database information ([428ea02](https://github.com/AtriusX/Waystones/commit/428ea027a0dbb37dd92c66028bb9b7bfed904d4c))
* Add temporary migration command for waystone names ([8af6796](https://github.com/AtriusX/Waystones/commit/8af67962dbfd36f472b32319a7ec6a0a4c8c9187))
* Fix detekt issues ([e8cb548](https://github.com/AtriusX/Waystones/commit/e8cb54862e4520c096597af50317b80c7af02df9))
* Improve migration function safety ([3156c52](https://github.com/AtriusX/Waystones/commit/3156c52e3917148c8740d7d130c8f7ccebf5a052))
* Introduce issue templates ([bf80bf5](https://github.com/AtriusX/Waystones/commit/bf80bf56afca116603f22f2bee3f1cee14039a2c))
* Introduce issue templates ([156b7bd](https://github.com/AtriusX/Waystones/commit/156b7bd94d12b1b090d21c36c151e8c9c28a7f1d))
* Minimize plugin shadowing ([60206cf](https://github.com/AtriusX/Waystones/commit/60206cf916bef88239e381919c8be6e9557ebab0))
* Minimize plugin shadowing ([9da5194](https://github.com/AtriusX/Waystones/commit/9da5194f6eb017e4e888ad2e4823771560a55547))

## [2.0.0](https://github.com/AtriusX/Waystones/compare/1.2.0...v2.0.0) (2025-07-26)


### ⚠ BREAKING CHANGES

* Make use of Mojang's Brigadier command system

### Features

* Add bossbar timer ([260756d](https://github.com/AtriusX/Waystones/commit/260756db2305faa790996829b0f69c45c0c74444))
* Add custom serializers and better formatting to properties ([0891ccd](https://github.com/AtriusX/Waystones/commit/0891ccdb14d6487930fa3d04297ac397b241017a))
* Add extra layer of locale de-specification ([88f3c7b](https://github.com/AtriusX/Waystones/commit/88f3c7b0595bf491c901e75460abf2e426e5c686))
* Add formatting support for properties ([25daa5a](https://github.com/AtriusX/Waystones/commit/25daa5ab2a1168f3b6334409f3b71e5963de17a4))
* Apply formatting to other properties ([decb877](https://github.com/AtriusX/Waystones/commit/decb877b4eb0fe0a9bd33588d030a6810faa767a))
* Clean up command menus ([a9280fe](https://github.com/AtriusX/Waystones/commit/a9280fed2b416989e825c1bdd4c7b11628f218cf))
* Implement new localization manager ([7e980a9](https://github.com/AtriusX/Waystones/commit/7e980a9ae7e90300be67955c0b6b8a58dd713fb1))
* Make use of Mojang's Brigadier command system ([b70c3c7](https://github.com/AtriusX/Waystones/commit/b70c3c7e309bcfee99635bf5303f9ba0cb757599))
* Reimplement world ratio system ([13342db](https://github.com/AtriusX/Waystones/commit/13342db6b9468480bc7b8c53f21455e358683933))


### Bug Fixes

* Distance limitations and localization ([b9511b3](https://github.com/AtriusX/Waystones/commit/b9511b3d41ddf6caea3dbcc48333292355d8b594))
* Distance limitations and localization ([80cf86d](https://github.com/AtriusX/Waystones/commit/80cf86d1268f25fd5059811f82c1bea9674c1511))
* Fix background texture for Waystones advancement window ([653778e](https://github.com/AtriusX/Waystones/commit/653778e0b41850053b77269ebb53200cb4f7cb40))
* Fix background texture for Waystones advancement window ([791fda0](https://github.com/AtriusX/Waystones/commit/791fda04bed09cf5483112beaf7d548ffe7bc013))
* Fix config serialization issue ([b157cfc](https://github.com/AtriusX/Waystones/commit/b157cfc687b3f7637aef6fbf2499d9a067fba872))
* Include permission checks ([2f5f290](https://github.com/AtriusX/Waystones/commit/2f5f290ea1afe68c21854a9bd0e6e3962328552e))
* Issue with config command breaking due to missing property info ([42fa83d](https://github.com/AtriusX/Waystones/commit/42fa83d5e65fb5489a007bf864255de37acccac7))
* Linking issue localization formatting ([6b9d550](https://github.com/AtriusX/Waystones/commit/6b9d550bb36cdeedf474afc421182a9c01b11883))
* Portal sickness chance inversion ([98e50cd](https://github.com/AtriusX/Waystones/commit/98e50cdf9f701c8d65836c014891dac903d53e35))
* Properly retrieve git pipeline build hash ([7f07d9b](https://github.com/AtriusX/Waystones/commit/7f07d9b8a18ca589c0e6f987914fba204a6adaf3))
* Properly retrieve git pipeline build hash ([c0f304d](https://github.com/AtriusX/Waystones/commit/c0f304da37b2a35e0da5b949a47eab302e549062))
* Severed waystone message, missing waystone names ([ecf673f](https://github.com/AtriusX/Waystones/commit/ecf673f0c3c88b2a6de342e04829ab6eed7b72f1))
