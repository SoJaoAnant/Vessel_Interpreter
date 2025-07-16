package com.interpreter.JVessel;

import java.util.List;

interface vessel_callable {
    int arity();
    Object call(interpreter interpreter, List<Object> arguments);
}
