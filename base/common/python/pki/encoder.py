import json

TYPES = {}
NOTYPES = {}

class CustomTypeEncoder(json.JSONEncoder):
    """A custom JSONEncoder class that knows how to encode core custom
    objects.

    Custom objects are encoded as JSON object literals (ie, dicts) with
    one key, 'TypeName' where 'TypeName' is the actual name of the
    type to which the object belongs.  That single key maps to another
    object literal which is just the __dict__ of the object encoded."""

    """Reason for ignoring the error
         E0202 - An attribute affected in json.encoder line 157 hide this method
       reported by pylint:

       The error is in json.encoder.JSONEncoder class.
       There is a default method (which is overridden here) and also a class attribute
       self.default initialized in the init method of the class.
       The intention of such usage being that a custom default method object can
       be passed to init when creating an instance of JSONEncoder, which is then assigned
       to class's default method. (which is valid)
       But pylint raises an issue due to the usage of same name for a method and an attribute
       in which case the attribute definition hides the method.
       The reason and example for the issue: (top rated comment)
           http://stackoverflow.com/questions/12949064/python-what-happens-
           when-instance-variable-name-is-same-as-method-name
    """
    # pylint: disable-msg=E0202
    def default(self, obj):
        for k, v in TYPES.items():
            if isinstance(obj, v):
                return {k: obj.__dict__}
        for k, v in NOTYPES.items():
            if isinstance(obj, v):
                return obj.__dict__
        return json.JSONEncoder.default(self, obj)


def CustomTypeDecoder(dct):
    if len(dct) == 1:
        type_name, value = dct.items()[0]
        if type_name in TYPES:
            return TYPES[type_name].from_dict(value)
    return dct
