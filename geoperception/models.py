import uuid
from cqlengine import columns
from cqlengine.models import Model

class Locations(Model):
    read_repair_chance = 0.05  # optional - defaults to 0.1
    name = columns.text(primary_key=True)
    count = columns.DateTime()
