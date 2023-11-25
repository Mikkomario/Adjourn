# Adjourn
Updated: 2023-11-24

## Table of Contents
- [Packages & Classes](#packages-and-classes)
  - [File](#file)
    - [Directory](#directory)
    - [File Index](#file-index)
    - [File Type](#file-type)

## Packages and Classes
Below are listed all classes introduced in Adjourn, grouped by package and in alphabetical order.  
There are a total number of 1 packages and 3 classes

### File
This package contains the following 3 classes: [Directory](#directory), [File Index](#file-index), [File Type](#file-type)

#### Directory
Represents a directory that may contain files

##### Details
- Combines with possibly multiple [File Indices](#file-index), creating a **Directory With Files**
- Uses a **combo index**: `parent_id` => `name`

##### Properties
Directory contains the following 2 properties:
- **Parent Id** - `parentId: Option[Int]` - Id of the directory that contains this directory. None if this is the root directory / drive.
  - Refers to [Directory](#directory)
- **Name** - `name: String`

##### Referenced from
- [Directory](#directory).`parentId`
- [File Index](#file-index).`directoryId`

#### File Index

##### Details
- Uses **2 combo indices**:
  - `type_id` => `size` => `name`
  - `directory_id` => `name`

##### Properties
File Index contains the following 5 properties:
- **Directory Id** - `directoryId: Int` - Id of the directory where this file resides
  - Refers to [Directory](#directory)
- **Name** - `name: String` - Name of this file
- **Type Id** - `typeId: Int` - Id that matches this file's type
  - Refers to [File Type](#file-type)
- **Size** - `size: Int` - Size of this file in bytes
- **Last Modified** - `lastModified: Option[Instant]` - Time when this file was last modified (may be out of date)

#### File Type
Represents a specific file type, such as a text file

##### Details
- Uses **index**: `name`

##### Properties
File Type contains the following 1 properties:
- **Name** - `name: String` - Name of this file type

##### Referenced from
- [File Index](#file-index).`typeId`
