export class AnnouncementListFilters{
  announcementType: {
    colDef?: string,
    type?: string,
    value: string,
  } | undefined = {
    colDef: 'announcementType',
    type: 'dropdown',
    value: '',
  };
  announcementTitle: {
    colDef?: string,
    type?: string,
    value: string,
  } | undefined = {
    colDef: 'announcementTitle',
    type: 'text',
    value: '',
  };
  min: {
    colDef: string,
    value: number,
    matchMode: string
  } | undefined = {
    colDef: 'min',
    value: 0,
    matchMode: 'equals'
  };
  max: {
    colDef: string,
    value: number,
    matchMode: string
  } | undefined = {
    colDef: 'max',
    value: 0,
    matchMode: 'equals'
  };
  status: {
    colDef?: string,
    type?: string,
    value: any,
  } | undefined = {
    colDef: 'status',
    type: 'equals',
    value: '',
  };
  from: {
    colDef?: string,
    type?: string,
    value: any,
  } | undefined = {
    colDef: 'from',
    type: 'date',
    value: '',
  };
  to: {
    colDef?: string,
    type?: string,
    value: any,
  } | undefined = {
    colDef: 'to',
    type: 'date',
    value: '',
  };

}
