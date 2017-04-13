/**
 * Created by Lamhm on 4/13/2017.
 */
QANT2X = {};
QANT2X.DataType = {
    NULL: 0,
    BOOL: 1,
    BYTE: 2,
    SHORT: 3,
    INT: 4,
    LONG: 5,
    FLOAT: 6,
    DOUBLE: 7,
    UTF_STRING: 8,
    BOOL_ARRAY: 9,
    BYTE_ARRAY: 10,
    SHORT_ARRAY: 11,
    INT_ARRAY: 12,
    LONG_ARRAY: 13,
    FLOAT_ARRAY: 14,
    DOUBLE_ARRAY: 15,
    UTF_STRING_ARRAY: 16,
    QANT_ARRAY: 17,
    QANT_OBJECT: 18
}

QANT2X.QAntDataSerializer = {

    object2binary: function (qAntObject) {
        var dataStream = new DataStream();
        dataStream.writeByte(QANT2X.DataType.QANT_OBJECT);
        dataStream.writeShort(qAntObject.size());
        return this.obj2bin(qAntObject, dataStream);
    },

    obj2bin: function (qAntObject, dataStream) {
        var keys = qAntObject.getKeys();
        for (var index in  keys) {
            var key = keys[index];
            var wrapper = qAntObject.get(key);
            var dataObj = wrapper.getObject();
            this.encodeQAntObjectKey(dataStream, key);
            this.encodeObject(dataStream, wrapper.getType(), dataObj);
        }
        return dataStream.getBytes();
    },

    encodeQAntObjectKey: function (dataStream, value) {
        dataStream.writeShort(value.length);
        dataStream.writeBytes(this.string2Bin(value));
    },

    encodeObject: function (dataStream, dataType, object) {
        switch (dataType) {
            case QANT2X.DataType.NULL:
                break;
            case QANT2X.DataType.INT:
                this.binEncode_INT(dataStream, object);
                break;
            case QANT2X.DataType.UTF_STRING:
                this.binEncode_UTF_STRING(dataStream, object);
                break;
            case QANT2X.DataType.LONG:
                this.binEncode_LONG(dataStream, object);
                break;

        }
    },

    binEncode_INT: function (dataStream, value) {
        dataStream.writeByte(QANT2X.DataType.INT);
        dataStream.writeInt(value);
    },

    binEncode_LONG: function(dataStream, value){
        dataStream.writeByte(QANT2X.DataType.LONG);
        dataStream.writein
    },

    binEncode_UTF_STRING: function (dataStream, value) {
        dataStream.writeByte(QANT2X.DataType.UTF_STRING);
        dataStream.writeShort(value.length);
        dataStream.writeBytes(this.string2Bin(value));
    },

    string2Bin: function (str) {
        var result = [];
        for (var i = 0; i < str.length; i++) {
            result.push(str.charCodeAt(i));
        }
        return result;
    }

}

/* ---------------------- QAnt Data Wrapper ----------------------*/
function QAntDataWrapper(type, object) {

    this.type = type;
    this.object = object;
}
QAntDataWrapper.prototype.getType = function () {
    return this.type;
};

QAntDataWrapper.prototype.getObject = function () {
    return this.object;
};


/* ---------------------- QAnt Object ----------------------*/

function QAntObject() {
    this.dataHolder = {};
}


QAntObject.prototype.putObj = function (key, value, dataType) {
    if (key == null) {
        console.log("[QANT_API]/////////////// key not null ///////////////");
        return;
    }

    if (key.length > 255) {
        console.log("[QANT_API]/////////////// keys must be less than 255 characters ///////////////");
        return;
    }

    if (value == null) {
        console.log("[QANT_API]/////////////// QAntObject requires a non-null value! If you need to add a null use the putNull() method. ///////////////");
        return;
    }

    if (value instanceof QAntDataWrapper) {
        this.dataHolder[key] = value;
    } else {
        this.dataHolder[key] = new QAntDataWrapper(dataType, value);
    }

}

QAntObject.prototype.putBoolArray = function (key, value) {
    this.putObj(key, value, QANT2X.DataType.BOOL_ARRAY);
}

QAntObject.prototype.putBool = function (key, value) {
    this.putObj(key, value, QANT2X.DataType.BOOL);
}

QAntObject.prototype.putByte = function (key, value) {
    this.putObj(key, value, QANT2X.DataType.BYTE);
}

QAntObject.prototype.putByteArray = function (key, value) {
    this.putObj(key, value, QANT2X.DataType.BYTE_ARRAY);
}

QAntObject.prototype.putDouble = function (key, value) {
    this.putObj(key, value, QANT2X.DataType.DOUBLE);
}

QAntObject.prototype.putInt = function (key, value) {
    this.putObj(key, value, QANT2X.DataType.INT);
}

QAntObject.prototype.putLong = function (key, value) {
    this.putObj(key, value, QANT2X.DataType.LONG);
}

QAntObject.prototype.putNull = function (key) {
    this.dataHolder[key] = new QAntDataWrapper(QANT2X.DataType.NULL, null);
}

QAntObject.prototype.putQAntObject = function (key, value) {
    this.putObj(key, value, QANT2X.DataType.QANT_OBJECT);
}

QAntObject.prototype.putShort = function (key, value) {
    this.putObj(key, value, QANT2X.DataType.SHORT);
}

QAntObject.prototype.putUtfString = function (key, value) {
    this.putObj(key, value, QANT2X.DataType.UTF_STRING);
}

QAntObject.prototype.put = function (key, wrappedObject) {
    this.putObj(key, wrappedObject, null);
}

QAntObject.prototype.get = function (key) {
    return this.dataHolder[key];
}

QAntObject.prototype.putBool = function (key, value) {
    this.putObj(key, value, QANT2X.DataType.BOOL);
}

QAntObject.prototype.putBool = function (key, value) {
    this.putObj(key, value, QANT2X.DataType.BOOL);
}

QAntObject.prototype.toBinary = function () {
    return QANT2X.QAntDataSerializer.object2binary(this);
}

QAntObject.prototype.getKeys = function () {
    return Object.keys(this.dataHolder);
}

QAntObject.prototype.size = function () {
    var count = 0;
    for (var prop in this.dataHolder) {
        if (this.dataHolder.hasOwnProperty(prop)) {
            ++count;
        }
    }

    return count;
}


