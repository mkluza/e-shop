import {createMuiTheme} from '@material-ui/core';


export const lightTheme = createMuiTheme({
	palette : {
		background : {
			default : "#ffffff",
		},
		text : {
			primary: "#ffffff",
		},
		primary : {
			main : 'rgb(62,102,167)',
			contrastText : "#E5E5E5",
		},
		secondary : {
			main : 'rgb(131,163,255)',
			contrastText : "#E5E5E5",
		},
	},

	typography : {
		fontFamily : "'Poppins', sans-serif",
		h2 : {
			textTransform: "none",
			fontWeight: 600,
		},
		h5 : {
			textTransform: "none",
			fontWeight: 600,
		},
	}

});

export default lightTheme;
