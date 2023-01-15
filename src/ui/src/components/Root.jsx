import React, {useEffect, useState} from 'react';
import {Route, Routes} from "react-router-dom";
import Library from "./books/Library";

const Root = () => {

    return (
        <div>
            <Routes>
                <Route path="/" exact element={
                        <Library/>
                    }
                />
            </Routes>
        </div>
    );
};

export default Root;