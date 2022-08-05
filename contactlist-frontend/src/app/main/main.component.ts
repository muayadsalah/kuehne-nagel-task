import {Component, ViewChild, AfterViewInit, EventEmitter} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort, SortDirection} from '@angular/material/sort';
import {merge, Observable, of as observableOf} from 'rxjs';
import {catchError, map, startWith, switchMap} from 'rxjs/operators';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.sass']
})
export class MainComponent implements AfterViewInit {
  displayedColumns: string[] = ['id', 'name', 'imageUrl'];
  exampleDatabase: ExampleHttpDatabase | null;
  data: Contacts[] = [];
  filteredData: Contacts[] = [];
  filterValueSubject = new EventEmitter<string>();
  filterValue: string = '';

  resultsLength = 0;
  isLoadingResults = true;
  isApiError = false;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private _httpClient: HttpClient) {}

  ngAfterViewInit() {
    this.exampleDatabase = new ExampleHttpDatabase(this._httpClient);

    // If the user changes the sort order, reset back to the first page.
    this.sort.sortChange.subscribe(() => (this.paginator.pageIndex = 0));

    merge(this.sort.sortChange, this.paginator.page, this.filterValueSubject)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.exampleDatabase!.getRepoIssues(
            this.sort.active,
            this.sort.direction,
            this.paginator.pageIndex,
            this.filterValue
          ).pipe(catchError(() => observableOf(null)));
        }),
        map(data => {
          // Flip flag to show that loading has finished.
          this.isLoadingResults = false;
          this.isApiError = data === null;

          if (data === null) {
            return [];
          }

          // Only refresh the result length if there is new data. In case of rate
          // limit errors, we do not want to reset the paginator to zero, as that
          // would prevent users from re-triggering requests.
          this.resultsLength = data.totalElements;
          return data.content;
        }),
      )
      .subscribe(data => {
        this.data = data
        this.filteredData = data
      });
  }

  applyFilter(event: Event) {
    this.filterValue = (event.target as HTMLInputElement).value;
    this.filterValueSubject.next(this.filterValue);
  }

}

export interface ContactsPageApi {
  content: Contacts[];

  totalPages: number;
  totalElements: number;
  last: boolean;
  first: boolean;
  empty: boolean;
  size: number;
  number: number;
}

export interface Contacts {
  id: number;
  name: string;
  imageUrl: string;
}

export class ExampleHttpDatabase {
  constructor(private _httpClient: HttpClient) {}

  getRepoIssues(sort: string, order: SortDirection, page: number, filterValue: string): Observable<ContactsPageApi> {
    const href = environment.SERVER_URL + 'v0/contacts';
    let requestUrl = `${href}?sort=${sort},${order}&page=${page}`;
    if (filterValue?.length) {
      requestUrl += `&q=${filterValue}`;
    }

    return this._httpClient.get<ContactsPageApi>(requestUrl);
  }
}

