# UT Converter Readme file

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/dec0f1a5176748a29195c99d7862339f)](https://app.codacy.com/gh/xtremexp/UT4X-Converter/dashboard)

## Description

- Name: UT Converter
- Author: Thomas 'XtremeXp/WinterIsComing' P.
- Download: [GitHub - Releases](https://github.com/xtremexp/UT4X-Converter/releases)
- Source Code: [GitHub](https://github.com/xtremexp/UT4X-Converter)


UT Converter is a program that **helps converting** maps from Unreal (Tournament) previous games
to UT3 (2007) and Unreal Tournament 4 (2015).

Notes :
- Conversion from non unreal/ut games might work, but it's not tested and won't be supported at all
- This is not a 'plug and play' program, user will have to do manual operations
using Unreal Editor to get results.



## UT games supported

Here are the different conversion possible :

- Source Game -> Dest Game (Conversion quality)
- U1/UT99/Duke Nukem Forever (2001) -> UT3/UT4 (Good)
- UT2003/2004 -> UT4 (Good)
- Unreal Championship 2/Unreal 2/UT3/UDK -> UT4 (Medium)
- X -> Unreal Engine 5 (Not Supported)

Notes:
- All: Custom scripts and blueprints are not converted
- All: Shader materials are not converted
- UT3: Since there is no actor possible for conversion of ZoneInfo, converted level might appear too dark sometimes
- U1/UT99 : Meshes (Unreal Engine 1/2) are not converted
- U2: Staticmeshes have bad UV
- UT3/UDK: Music is not converted
- UTx->UT4: Movers are replaced with the only UT4 mover actor, the lift, which may not suit for some kind of movers (such as doors, switches, ...)


---

## Requirements

- Windows 7/8/10/11 - 64 Bit
- UT3 or UT4 editor for output converted map
- Any Unreal (Tournament) game for input map
- Some minimum skills with unreal engine editors

## Installation and quick start

------------------------------

- Install program by clicking on .exe file
- Launch program (.exe file)
- Go to Settings and set the unreal games folders
- Convert your map in Menu: File -> Unreal (Tournament) -> Convert Map to UT4/UT3 ...

Program Update
------------------------------
- Uninstall program in windows applications configuration panel
- Install new version (.exe file)

## How to convert a map?
Check wiki page at: [UT Converter Wiki](https://github.com/xtremexp/UT4X-Converter/wiki)


---

## Build and run from source code

### Prerequisites
- [Git](https://gitforwindows.org/)
- Java 21 (recommended: [OpenJDK 21](https://jdk.java.net/21/) or [Eclipse Temurin 21](https://adoptium.net/))

### Quick Start
1. Clone the project:
   ```bash
   git clone https://github.com/xtremexp/UT4X-Converter
   cd UT4X-Converter
   ```

2. **Option A: Using Maven Wrapper (Recommended)**
   ```bash
   # Build and run the application
   ./mvnw clean javafx:run
   
   # Package the application (.exe installer will be generated in target/package folder on Windows)
   ./mvnw clean install
   
   # Run tests
   ./mvnw test
   
   # Check for security vulnerabilities
   ./mvnw dependency-check:check
   ```

3. **Option B: Using System Maven**
   - Install [Maven](https://maven.apache.org/) (version 3.6.3 or higher)
   - Build and run:
     ```bash
     mvn clean javafx:run
     mvn clean install
     ```

### Development Notes
- UI (.fxml files) can be easily modified with [Scene Builder editor](https://gluonhq.com/products/scene-builder/)
- ExtractTextures.exe and UtxAnalyser.exe programs have been compiled using modified source code from "UT Package Delphi Unit" by Antonio Corbero
  - Download and install [Delphi Community Edition](https://www.embarcadero.com/products/delphi/starter)
  - Download and extract [UT Package Delphi Unit](https://www.acordero.org/projects/unreal-tournament-package-delphi-library/)
  - Add the /src/delphi/-.dpr files where you installed UT Package Delphi Unit
  - Open ExtractTextures.dpr or UtxAnalyser.dpr with the IDE

### Build Requirements
- **Java**: 21 (LTS)
- **Maven**: 3.6.3 or higher (or use included Maven Wrapper)
- **OS**: Windows 7/8/10/11 (64-bit) for full packaging support

## External programs used
These programs are being used during conversion process.

| Library / Program                     | Description                                       | Author                                                                | Website                                         |
|---------------------------------------|---------------------------------------------------|-----------------------------------------------------------------------| ----------------------------------------------- |
| Texture Toolkit<br/>(g16convert.exe)  | Unreal terrain bitmap conversion                  | Alex Stewart                                                          | http://www.foogod.com/UEdTexKit/                |
| UModel  (umodel_64.exe)               | Unreal package extractor                          | Konstantin Nosov                                                      | http://www.gildor.org/en/projects/umodel        |
| SoundExchange (sox.exe)               | Sound wave conversion                             | Multiple                                                              | https://sox.sourceforge.net/                                             |
| UTXAnalyser.exe / ExtractTextures.exe | Texture info / Texture extract<br/>(for Unreal 2) | Me / Modified code of UT Package Delphi Unit by <br/>Antonio Acordero | https://www.acordero.org/projects/unreal-tournament-package-delphi-library/                                                                         |



## License

See LICENSE file.

## History
See history.md for complete changes history
