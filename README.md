[![Build Status](https://travis-ci.org/CUBigDataClass/GeoPerceptionApp.svg?branch=test-integration)](https://travis-ci.org/CUBigDataClass/GeoPerceptionApp)
# GeoPerception

## Project Description
**Team Name:** GeoPerception

**Team Members:**
    Brandon Mikulka,
    Michael Asnes,
    John-Luke Bucuvalas,
    Cassiane Cladis, and
    Jacob Morales

**Project:** Analyze Geodata from Twitter (or other similar geotagged sources) to map trends in hashtags or related keywords throughout a history.

**Extended Idea:** Use historical trends to predict trends within that keyword. For example, we might be able to analyze keywords that are commonly used when using the hashtag “#depressed” to map out behavior of a tweeter that may be depressed. In this way we can aggregate data throughout history and see trends in populations and correlate them to events happening in real life (maybe it has been cloudy for a month straight).

## Workflow
* Checkout to a new feature branch
* Commit you work to that branch
* Push that branch to Github
* Create a Pull Request to merge back
* Once the team approves and all tests pass, we merge!

## Installation

### 1. virtualenv / virtualenv wrapper
`virtualenv` is a python environment manager that allows you to create an isolated python environment inside a folder. This feature combined with `virtualenvwrapper` allows you to create the same virtual environment, but store it in a .virtualenvs folder in your home directory, rather than scattered throughout your computer in different folders.

You will need to install python3

```bash
brew install python3 # on a mac
apt-get install python3 # on ubuntu/debian
```


```bash
pip install virtualenv virtualenvwrapper
```

Once installed, use the command:

```
mkvirtualenv -p $(which python3) geoperception
```

This creates an isolated environment called 'geoperception' that is running python 3.

Use the following commmand to start working within that environment:

```bash
workon geoperception
```

When you are finished, you can close the tab or use the `deactivate` command to return to your normal python environment.

### 2. Download the project
You will need to clone the repository to your workspace

```bash
cd /path/to/your/workspace
git clone git@github.com:CUBigDataClass/GeoPerceptionApp.git
cd GeoPerceptionApp
```

### 3. Install Requirements
The requirements.txt file will allow you to install django and this App's dependencies in one command (make sure that you are inside your virtualenvwrapper environment):

```bash
pip install -r requirements.txt
```

### 4. Get Cassandra running
There is a doc page dedicated to getting cassandra working with this application
[Cassandra Readme](docs/cassandra_readme.md)

### 5. Ready to rock and roll
Lets get the thing up and running

```bash
./manage.py migrate
./manage.py runserver
```

#### Testing Production
If you want to run the server without debug mode:

```bash
./manage.py runserver --settings=geoperception.settings.production
```
