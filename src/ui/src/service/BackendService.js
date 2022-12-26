import axios from "axios";
import {getApiUrl} from "./Settings";

const SAVE_BOOK = '/save';

export default {

    async saveBook() {
        try {
            await axios.post(getApiUrl(SAVE_BOOK));
        } catch (e) {
            console.log("something went wrong!", e);
        }
    },

    showAllBooks() {
        return fetch('/show_all')
    }
}