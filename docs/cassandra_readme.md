# Notes for getting Cassandra to work with Django/VirtualEnvWrapper

## Mac

```
brew install cassandra
```

Make sure `cassandra-driver` and `cql` are installed with pip inside the virtual environment. You can use `pip list` to see. These should be included in the requirements.txt file.

We will need to set the `PYTHONPATH` variable, so that it is able to see the cassandra driver. This can be done when VirtualEnvWrapper is activated with a `workon geoperception`

You will need to edit the `postactivate` and `predeactivate`

These two files will be found in:
`/Users/<YOUR_USERNAME>/.virutalenvs/geoperception/bin/`

#### postactivate

```bash
export PYTHONPATH_OLD=`$PYTHONPATH`
export PYTHONPATH="/Users/<YOUR_USERNAME_GOES_HERE>/.virutalenvs/geoperception/lib/python3.4/site-packages"
```

This will set your current python path to a variable for retrieval when you deactivate


#### predeactivate

```bash
export PYTHONPATH=`$PYTHONPATH_OLD`
```

This will undo your temporary python path in case you had something in there before activating.

If you have done everything correctly, you should be able to open your virtual environment for python and run `cqlsh` to connect to cassandra
