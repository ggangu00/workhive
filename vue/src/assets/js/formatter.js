import { dateTimeFormat } from '../../assets/js/common';

// formatter
export const timeFormatter = ({ value }) => dateTimeFormat(value, 'MM/dd hh:mm');
export const dateFormatter = ({ value }) => dateTimeFormat(value, 'yyyy-MM-dd');
export const signFormatter = ({ value }) => {
  let signState = '';
  switch(value) {
    case "D01":
      signState = "미결재";
      break;
    case "D02":
      signState = "승인";
      break;
    case "D03":
      signState = "보완";
      break;
    case "D04":
      signState = `<a>반려</a>`;
      break;
  }
  return signState;
}