import './App.css';
import { useEffect, useState } from 'react';
import * as React from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import { Alert, Button, CircularProgress, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, MenuItem, Select } from '@mui/material';
import CheckIcon from '@mui/icons-material/Check';
const columns = [
  { index: 0, id: 'id', label: 'Id', minWidth: 30 },
  { index: 1, id: 'description', label: 'Description', minWidth: 80 },
  {
    index: 2,
    id: 'date',
    label: 'Date(YYYY-MM-DD)',
    minWidth: 50,
  },
  {
    index: 3,
    id: 'amount',
    label: 'Amount(CAD)',
    minWidth: 30,
    align: 'right'
  },
  {
    index: 4,
    id: 'categoryId',
    label: 'Category',
    minWidth: 100
  },
  {
    index: 5,
    id: 'action',
    label: 'Action',
    minWidth: 100,
  }
];


function App() {
  const [data, setData] = useState(null);
  const [categories, setCategories] = useState(null);
  const [loading, setLoading] = useState(true);
  const [successMessage, setSuccessAlertMessage] = useState('');
  const [errorMessage, setErrorAlertMessage] = useState('');

  useEffect(() => {
    loadTrans();
  }, []);

  const loadTrans = () => {
    setLoading(true);
    fetch("http://localhost:8080/categories")
      .then(res => res.json())
      .then(res => {
        setCategories(res);
      }, error => {
        setErrorAlertMessage('failed to fetch categories' + error);
      });
    fetch("http://localhost:8080/transactions")
      .then(res => res.json())
      .then(res => {
        setData(res);
        setLoading(false);
      }, error => {
        setErrorAlertMessage(error);
        setLoading(false);
      });
  }

  const callSplitwise = (tran) => () => {
    console.log(tran);
    fetch('http://localhost:8080/sendtosplitwise/' + tran.id, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      }
    })
      .then(res => {
        console.log(res);
        if (res.status === 200) {
          data.find(i => i.id === tran.id).filedWithSplitwise = true;
          setData([...data]);
          setSuccessAlertMessage('Successfully sent to Splitwise');
          setTimeout(() => {
            setSuccessAlertMessage('');
          }, 1000);
        } else {
          setErrorAlertMessage('Could not send to splitwise');
        }
      });
  }
  const handleCategoryChange = (id) => ({ target: { value: categoryId } }) => {
    console.log('rowId=', id, 'categoryId', categoryId);
    fetch('http://localhost:8080/savecategory/' + id + '?categoryId=' + categoryId, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      }
    })
      .then(res => res.json())
      .then(res => {
        data.find(i => i.id === res.id).categoryId = res.categoryId;
        setData([...data]);
      }, error => {
        setErrorAlertMessage('Could not save category');
      });
  }

  const renderCell = (row, column) => {
    const { label, id } = column;
    const cellValue = row[id];
    if (label === 'Category') {
      return <Select
        id={'category_select_' + row.id}
        key={'category_select_' + row.id}
        value={cellValue === null ? "" : cellValue}
        label="Category"
        onChange={handleCategoryChange(row.id)}
      >
        {
          categories.map(c => (<MenuItem key={row.id + c.id} value={c.id}>{c.name}</MenuItem>))
        }
      </Select>
    } else if (label === 'Action') {
      if (row.filedWithSplitwise !== true && row.categoryId !== 100) {
        return <Button onClick={callSplitwise(row)}>Send to Splitwise</Button>
      }
      return <CheckIcon />;
    }
    else {
      return cellValue;
    }
  }

  return (
    
      <div className="App">
        <br />
        <br />
        <form action="http://localhost:8080/savetransactions" target="uploadIframe" encType="multipart/form-data" method='POST' >
          <input type="file" name="csvFile" />
          <button type="submit">Upload CSV File</button>
        </form>
        <iframe name="uploadIframe" title="Uploaded file" />
        <button onClick={loadTrans}>Refresh Transactions</button>
        <hr />

        <Dialog
          open={errorMessage !== ''}
          onClose={() => { setErrorAlertMessage('') }}
        >
          <DialogTitle >
            {"Error"}
          </DialogTitle>
          <DialogContent>
            <DialogContentText>
              {errorMessage}
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={() => { setErrorAlertMessage('') }}>OK</Button>
          </DialogActions>
        </Dialog>

        {successMessage !== '' && <Alert severity="success">
          {successMessage}
        </Alert>}
        {
          loading ? <CircularProgress /> :
            (

              <Paper sx={{ width: '100%', overflow: 'hidden' }}>
                <TableContainer sx={{ maxHeight: 550 }}>
                  <Table stickyHeader aria-label="sticky table">
                    <TableHead>
                      <TableRow>
                        {columns.map((column) => (
                          <TableCell
                            key={column.id}
                            align={column.align}
                            style={{ minWidth: column.minWidth }}
                          >
                            {column.label}
                          </TableCell>
                        ))}
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {data
                        .map((row) => {
                          return (
                            <TableRow hover role="checkbox" tabIndex={-1} key={'row' + row.id}>
                              {columns.map((column) => {
                                return (
                                  <TableCell key={'cell' + row.id + column.index} align={column.align}>
                                    {renderCell(row, column)}
                                  </TableCell>
                                );
                              })}
                            </TableRow>
                          );
                        })}
                    </TableBody>
                  </Table>
                </TableContainer>
              </Paper>

            )

        }

      </div>
    
  );


}



export default App;
